package org.cloudbus.cloudsim.examples;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class Experiment {
	private static List<Cloudlet> cloudeletlist;
	private static List<Vm> vmlist;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			
		FileWriter writefile;
		try {
			writefile = new FileWriter("I:\\Makespan.csv");
			writefile.append("Vm Count");
			writefile.append(",");
			writefile.append("Makespan Time");
			writefile.append("\n");
		
			for(int i=0;i<=1;i++ ) {
				int x=1;
				Log.print("Number of iteration is "+ i);
				int user=1;
				Calendar calendar =Calendar.getInstance();
				boolean trace_flag = false;
				CloudSim.init(user, calendar, trace_flag);
				Datacenter datacenter = createdatacenter("Datacenter_0");
				DatacenterBroker datacenterbrooker = createdatacenterbroker();
				
				int brokerid = datacenterbrooker.getId();
				vmlist = createVM(brokerid,i+1);
				cloudeletlist = createCloudlet(brokerid,500);
				
				datacenterbrooker.submitVmList(vmlist);
				datacenterbrooker.submitCloudletList(cloudeletlist);
				
				CloudSim.startSimulation();
				List<Cloudlet> newlist = datacenterbrooker.getCloudletReceivedList();
				CloudSim.stopSimulation();
				
				writefile.append(Integer.toString(i));
				writefile.append(",");
				writefile.append(Double.toString(newlist.get(newlist.size() - 1).getWallClockTime()));
				writefile.append("\n");
				x++;
				printcloudletlist(newlist);
			}
			writefile.flush();
			writefile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void printcloudletlist(List<Cloudlet> list) {
		// TODO Auto-generated method stub
		int size = list.size();
		Cloudlet cloudlet;

		
		try {
			FileWriter writefile;
			writefile = new FileWriter("I:\\Flow_delay.csv");
			writefile.append("Vm Count");
			writefile.append(",");
			writefile.append("Flow Time");
			writefile.append(",");
			writefile.append("Delay Time");
			writefile.append("\n");
			for (int i = 0; i < size; i++) {
				cloudlet = list.get(i);
				
				writefile.append(Integer.toString(i));
				writefile.append(",");
				writefile.append(Double.toString(cloudlet.getActualCPUTime()+cloudlet.getWaitingTime()));
				writefile.append(",");
				writefile.append(Double.toString((cloudlet.getActualCPUTime()+cloudlet.getSubmissionTime()) - 0.5));
				writefile.append("\n");
			}	
			writefile.flush();
			writefile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
				"Data center ID" + indent + "VM ID" + indent + indent + "Flow Time" + indent + "Start Time" 
				+ indent + "Finish Time"+ indent + "Waiting Time"+ indent + "Delay Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
				Log.print("SUCCESS");

				Log.printLine( indent + indent + cloudlet.getResourceId() + 
						indent + indent + indent + cloudlet.getVmId() +
						indent + indent + indent + dft.format(cloudlet.getActualCPUTime()+cloudlet.getWaitingTime()) +
						indent + indent + dft.format(cloudlet.getExecStartTime())+ 
						indent + indent + indent + dft.format(cloudlet.getFinishTime())+ 
						indent + indent + indent + dft.format(cloudlet.getFinishTime()-cloudlet.getActualCPUTime())+
						indent + indent + indent + dft.format((cloudlet.getActualCPUTime()+cloudlet.getSubmissionTime()) - 0.5)+
						indent + indent + indent + dft.format(cloudlet.getSubmissionTime()));
			}
		}	
		
	}

	private static List<Cloudlet> createCloudlet(int userid, int cloudlets) {
		// TODO Auto-generated method stub
		LinkedList<Cloudlet> cloudlist = new LinkedList<>();
		long[] length = {11064,12996,11068,12977,11083,12967,11089,12961,11101,12957,
				11104,12955,11109,12899,11120,12895,11132,12860,
				11948,11923,11859,11849,11840,11838,11828,11807,11804,11793,11746,11720,
				11613,11585,11567,11564,11536,11523,11488,11483,11480,11474,11421,11419,11412,11404,
				1363,1359,1344,1317,1316,1302,1253,1237,1229,1213,1193,1180,1114,1109,
				1164,1160,1190,1154,1191,1051,1058,1054,1037,1030,1015,1091,1087,1064,
				1361,1361,1350,1345,1267,1260,12777,13945,11001,1101,
				1567,1564,1536,1523,1488,1143,1180,1174,1421,1419,11412,1144,
				1999,1888,1777,1166,1555,1444,1333,12345,2345,8900,12987,6543,9832,45632,32190,
				10543,15890,3654,23005,49321,16902,1902,2341,9044,7623,2139,8065,21904,7689,
				11064,12996,11068,12977,11083,12067,123089,11161,11101,12957,
				11104,12955,11109,12899,11120,128195,11923,12980,
				11948,11923,11859,11849,11840,11838,11828,11807,11804,11793,11746,11720,
				11613,11585,11567,11564,11536,11523,11488,11483,11480,11474,11421,11419,11412,11404,
				13363,13359,13344,13517,13516,13602,12583,12937,12129,13213,14193,12180,1114,11709,
				11464,15160,11790,11054,13191,14051,12058,12054,10937,10430,15015,12091,12087,12064,
				13641,13461,13550,13645,12678,12608,12777,13945,11001,11041,
				13667,13664,13376,15823,11488,11435,11680,13174,14521,14619,11412,12144,
				1999,1888,1777,1166,1555,1444,1333,12345,2345,8900,12987,6543,9832,45632,2190,
				10543,15890,10654,23000,49321,56902,1902,2341,9044,7623,2139,18065,21904,7689,
				11064,12996,11068,12977,11083,12967,11089,12961,11101,12957,
				11104,12955,11109,12899,11120,12895,11132,12860,
				11948,11923,11859,11849,11840,11838,11828,11807,11804,11793,11746,11720,
				11613,11585,11567,11564,11536,11523,11488,11483,11480,11474,11421,11419,11412,11404,
				1363,1359,1344,1317,1316,1302,1253,1237,1229,1213,1193,1180,1114,1109,
				1164,1160,1190,1154,1191,1051,1058,1054,1037,1030,1015,1091,1087,1064,
				1361,1361,1350,1345,1267,1260,12777,13945,11001,1101,
				1567,1564,1536,1523,1488,1143,1180,1174,1421,1419,11412,1144,
				1999,1888,1777,1166,1555,1444,1333,12345,2345,8900,12987,6543,9832,45632,32190,
				10543,15890,10654,23005,49321,56902,1902,2341,9044,7623,2139,18065,21904,7689,
				11064,12996,11068,12977,111083,12067,123089,11161,11101,12957,
				11104,12955,11109,12899,11120,128195,11923,12980,
				11948,11923,11859,11849,11840,11838,11828,11807,11804,11793,11746,11720,
				11613,11585,11567,11564,11536,11523,11488,11483,11480,11474,11421,11419,11412,11404,
				13363,13359,13344,13517,13516,13602,12583,12937,12129,13213,14193,16180,1114,11709,
				11464,15160,11790,11054,13191,14051,18058,18054,10937,10430,15015,12091,12087,12064,
				13641,13461,13550,13645,12678,12608,12777,13945,11001,11041,
				15667,15664,15376,15823,114488,11435,112680,13174,14521,14619,11412,12144,
				1999,1888,17757,11266,15655,14484,13393,12345,82345,8900,12987,6543,8532,45632,32190,
				10543,65890,30654,23000,49321,56902,19302,23141,9044,7623,2139,18065,21904,7689,
				
				
//				11034,11996,13068,14977,21083,52967,61089,16961,15101,19957,
//				19004,19955,18709,12699,14320,12595,13132,12890,
//				14948,15423,18959,19049,13240,13438,15428,19007,17804,11993,13746,15720,
//				12613,13585,11967,19564,14536,154523,17488,90483,67480,11774,51421,43419,16512,19404,
//				13563,15359,16344,12317,13316,11302,16253,17237,18229,18213,16193,11180,12114,13109,
//				11464,11760,11290,11554,11491,10651,10458,10354,10537,10530,10215,10191,10387,10564,
//				13461,11361,14350,14345,12267,15260,12777,13945,11001,11101,
//				15617,15647,15366,15243,14388,11343,11180,11274,14521,16419,11412,14144,
//				19992,13888,14777,11666,15655,14464,13533,12345,23345,89030,12987,65343,98332,44632,32190,
//				90543,615890,30654,23005,49321,56902,19022,23441,90444,76243,21349,18065,21904,76849,
//				11064,12996,11068,12977,111083,12067,123089,11161,11101,12957,
//				11104,12955,11109,12899,11120,128195,11923,12980,
//				11948,11923,11859,11849,11840,11838,11828,11807,11804,11793,11746,11720,
//				11613,11585,11567,11564,11536,11523,11488,11483,11480,11474,11421,11419,11412,11404,
//				13363,13359,13344,13517,13516,13602,12583,12937,12129,13213,14193,16180,1114,11709,
//				11464,15160,11790,11054,13191,14051,18058,18054,10937,10430,15015,12091,12087,12064,
//				13641,13461,13550,13645,12678,12608,12777,13945,11001,11041,
//				15667,15664,15376,15823,11488,11435,11680,13174,14521,14619,11412,12144,
//				1999,1888,1777,11661,15525,14444,13353,12345,25345,8900,12987,65453,98432,45632,32190,
//				90543,65890,30654,23000,49321,56902,19022,23412,90442,76233,21393,18065,21904,76389,
//				11064,12996,11068,12977,11083,12967,11089,12961,11101,12957,
//				11104,12955,11109,12899,11120,12895,11132,12860,
//				11948,11923,11859,11849,11840,11838,11828,11807,11804,11793,11746,11720,
//				11613,11585,11567,11564,11536,11523,11488,11483,11480,11474,11421,11419,11412,11404,
//				34563,13595,13424,13174,13163,13402,12534,12374,12299,12137,11935,11870,711542,11039,
//				11646,11640,11905,11544,11913,10531,10358,10254,12037,12030,10415,10491,14087,14064,
//				13615,13641,13540,13445,12467,1260,12777,13945,11001,11501,
//				15674,15644,15436,13523,14488,14143,12180,13174,13421,13419,131412,11644,
//				19939,18838,17747,11626,15525,14444,13333,12345,23453,89002,12987,65434,98324,45632,32190,
//				90543,615890,30654,23005,49321,56902,1902,2341,9044,7623,2139,18065,21904,71689,
//				11064,12996,11068,12977,111083,12067,123089,11161,11101,12957,
//				11104,12955,11109,12899,11120,128195,11923,12980,
//				11948,11923,11859,11849,11840,11838,11828,11807,11804,11793,11746,11720,
//				11613,11585,11567,11564,11536,11523,11488,11483,11480,11474,11421,11419,11412,11404,
//				13363,13359,13344,13517,13516,13602,12583,12937,12129,13213,14193,16180,1114,11709,
//				11464,15160,11790,11054,13191,14051,18058,18054,10937,10430,15015,12091,12087,12064,
//				13641,13461,13550,13645,12678,12608,12777,13945,11001,11041,
//				15667,15664,15376,15823,114488,11435,112680,13174,14521,14619,11412,12144,
//				1999,1888,17757,11266,15655,14484,13393,12345,82345,8900,12987,6543,98532,45632,32190,
//				90543,65890,30654,23000,49321,56902,19302,23141,9044,7623,2139,18065,21904,7689,
//				
//				
//				51000,53000,55000,103000,117000,59000,27500,125000,71000,59000,150000,93000,65000,107000,67000,
//				59000,15000,89000,103000,63000,125000,712500,337500,105000,65000,49000,95000,337500,47000,105000,
//				77000,71000,55000,103000,59000,63000,109000,107000,119000,85000,85000,61000,107000,61000,525000,
//				47000,91000,109000,65000,123000,45000,55000,121000,97000,123000,900000,47000,105000,129000,
//				93000,113000,115000,115000,15000,59000,115000,81000,115000,109000,111000,117000,27500,125000,
//				105000,77000,99000,127000,71000,77000,79000,87000,45000,99000,97000,93000,45000,121000,71000,
//				712500,900000,135000,85000,85000,75000,525000,89000,337500,45000,81000,115000,119000,59000,93000,
//				117000,65000,103000,40000,525000,127000,15000,45000,47000,89000,85000,103000,111000,135000,127000,
//				712500,150000,55000,91000,40000
				
				
				10034,10996,11068,11977,11083,12967,21089,10961,11101,13957,
				13004,13955,12709,12699,12320,11595,11132,11890,
				10948,10423,11959,12049,13240,13438,10428,11007,12804,11993,13746,11720,
				12613,13585,11967,12564,14536,154523,17488,10483,17480,11774,11421,13419,12512,12404,
				13563,15359,11344,12317,13316,11302,16253,17237,12229,12213,16193,11180,12114,13109,
				11464,11760,11290,11554,11491,10651,10458,10354,10537,10530,10215,10191,10387,10564,
				13461,11361,14350,14345,12267,13260,12777,13945,11001,11101,
				15617,15647,15366,15243,14388,11343,11180,11274,14521,16419,11412,14144,
				12992,13888,14777,11666,15655,14464,13533,12345,13345,19030,12987,15343,18332,14632,12190,
				10543,15890,10654,23005,19321,16902,11022,23441,10444,16243,21349,18065,11904,16849,
				11064,12996,11068,12977,11083,12067,23089,1161,1101,12957,
				11104,12955,11109,12899,11120,12195,11923,12980,
				11948,11923,11859,11849,11840,11838,11828,11807,11804,11793,11746,11720,
				11613,11585,11567,11564,11536,11523,11488,11483,11480,11474,11421,11419,11412,11404,
				13363,13359,13344,13517,13516,13602,12583,12937,12129,13213,14193,16180,1114,11709,
				11464,15160,11790,11054,13191,14051,18058,18054,10937,10430,15015,12091,12087,12064,
				13641,13461,13550,13645,12678,12608,12777,13945,11001,11041,
				15667,15664,15376,15823,11488,11435,11680,13174,14521,14619,11412,12144,
				1999,1888,1777,11661,15525,14444,13353,12345,15345,8900,12987,25453,18432,15632,12190,
				10543,15890,10654,13000,19321,16902,19022,23412,10442,16233,21393,18065,11904,16389,
				11064,12996,11068,12977,11083,12967,11089,12961,11101,12957,
				11104,12955,11109,12899,11120,12895,11132,12860,
				11948,11923,11859,11849,11840,11838,11828,11807,11804,11793,11746,11720,
				11613,11585,11567,11564,11536,11523,11488,11483,11480,11474,11421,11419,11412,11404,
				14563,13595,13424,13174,13163,13402,12534,12374,12299,12137,11935,11870,71542,11039,
				11646,11640,11905,11544,11913,10531,10358,10254,12037,12030,10415,10491,14087,14064,
				13615,13641,13540,13445,12467,1260,12777,13945,11001,11501,
				15674,15644,15436,13523,14488,14143,12180,13174,13421,13419,13112,11644,
				19939,18838,17747,11626,15525,14444,13333,12345,13453,19002,12987,15434,98324,15632,12190,
				10543,65890,30654,23005,49321,56902,1902,2341,9044,7623,2139,18065,21904,71689,
				11064,12996,11068,12977,111083,12067,123089,11161,11101,12957,
				11104,12955,11109,12899,11120,12195,11923,12980,
				11948,11923,11859,11849,11840,11838,11828,11807,11804,11793,11746,11720,
				11613,11585,11567,11564,11536,11523,11488,11483,11480,11474,11421,11419,11412,11404,
				13363,13359,13344,13517,13516,13602,12583,12937,12129,13213,14193,16180,1114,11709,
				11464,15160,11790,11054,13191,14051,18058,18054,10937,10430,15015,12091,12087,12064,
				13641,13461,13550,13645,12678,12608,12777,13945,11001,11041,
				15667,15664,15376,15823,14488,11435,12680,13174,14521,14619,11412,12144,
				1999,1888,17757,11266,15655,14484,13393,12345,82345,8900,12987,6543,81532,15632,21190,
				10543,15890,10654,23000,49321,56902,19302,23141,9044,7623,2139,18065,21904,7689,
				
				
				51000,53000,55000,13000,17000,59000,27500,12500,71000,59000,15000,93000,65000,10700,67000,
				59000,15000,89000,13000,63000,15000,71500,337500,105000,65000,49000,95000,337500,47000,105000,
				77000,71000,55000,103000,59000,63000,109000,107000,119000,85000,85000,61000,17000,61000,55000,
				47000,91000,19000,65000,123000,45000,55000,12000,97000,13000,90000,47000,15000,19000,
				93000,13000,15000,15000,15000,59000,15000,81000,15000,19000,11000,11000,7500,25000,
				105000,77000,99000,17000,71000,77000,79000,87000,45000,99000,17000,93000,45000,21000,71000,
				72500,90000,13500,85000,85000,75000,525000,89000,337500,45000,81000,115000,19000,59000,93000,
				17000,65000,10000,40000,25000,17000,15000,45000,47000,89000,85000,13000,11000,35000,27000,
				72500,15000,55000,91000,40000
				};
		
		long fileSize = 300;
		long outputSize = 300;
		
//		long fileSize = 300;
//		long outputSize = 300;
		
		int pesNumber = 1;
		UtilizationModelFull utilizationmodel = new UtilizationModelFull();
		Cloudlet[] cloudlet = new Cloudlet[cloudlets];
		
		for(int i = 0;i<cloudlets;i++) {
			Random random = new Random();
			cloudlet[i]= new Cloudlet(i, length[i], pesNumber, fileSize,
					outputSize, utilizationmodel, utilizationmodel, 
					utilizationmodel);
			cloudlet[i].setUserId(userid);
			//Log.printLine(cloudlet[i].getActualCPUTime());
			cloudlist.add(cloudlet[i]);
		}
		return cloudlist;
	}

	private static List<Vm> createVM(int userid, int vms) {
		// TODO Auto-generated method stub
		LinkedList<Vm> vmlist = new LinkedList<>();
		
		long size = 10000; //image size (MB)
		int ram = 512; //vm memory (MB)
//		int mips = 250;
		int mips = 225;
		long bw = 1000;
		int pesNumber = 1; //number of cpus
		String vmm = "Xen";
		
		Vm[] vm = new Vm[vms];
		for(int i = 0;i<vms;i++) {
			Log.print(i);
			vm[i]= new Vm(i, userid, mips, pesNumber, ram, bw, size, vmm,
					new CloudletSchedulerSpaceShared());
			vmlist.add(vm[i]);
		}
		return vmlist;
	}

	private static DatacenterBroker createdatacenterbroker() {
		// TODO Auto-generated method stub
		DatacenterBroker broker = null;
		
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return broker;
	}

	private static Datacenter createdatacenter(String name) {
		// TODO Auto-generated method stub
		List<Host> hostList = new ArrayList<Host>();

		// 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
		//    create a list to store these PEs before creating
		//    a Machine.
		List<Pe> peList1 = new ArrayList<Pe>();
		
		int mips = 1000;

		// 3. Create PEs and add these into the list.
		//for a quad-core machine, a list of 4 PEs is required:
		peList1.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
		peList1.add(new Pe(1, new PeProvisionerSimple(mips)));
		peList1.add(new Pe(2, new PeProvisionerSimple(mips)));
		peList1.add(new Pe(3, new PeProvisionerSimple(mips)));

		//Another list, for a dual-core machine
		List<Pe> peList2 = new ArrayList<Pe>();

		peList2.add(new Pe(0, new PeProvisionerSimple(mips)));
		peList2.add(new Pe(1, new PeProvisionerSimple(mips)));

		//4. Create Hosts with its id and list of PEs and add them to the list of machines
		int hostId=0;
		int ram = 16384; //host memory (MB)
		long storage = 1000000; //host storage
		int bw = 10000;

		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList1,
    				new VmSchedulerTimeShared(peList1)
    			)
    		); // This is our first machine

		hostId++;

		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList2,
    				new VmSchedulerTimeShared(peList2)
    			)
    		); // Second machine

		// 5. Create a DatacenterCharacteristics object that stores the
		//    properties of a data center: architecture, OS, list of
		//    Machines, allocation policy: time- or space-shared, time zone
		//    and its price (G$/Pe time unit).
		String arch = "x86";      // system architecture
		String os = "Linux";          // operating system
		String vmm = "Xen";
		double time_zone = 10.0;         // time zone this resource located
		double cost = 3.0;              // the cost of using processing in this resource
		double costPerMem = 0.05;		// the cost of using memory in this resource
		double costPerStorage = 0.1;	// the cost of using storage in this resource
		double costPerBw = 0.1;			// the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datacenter;
	}

}
