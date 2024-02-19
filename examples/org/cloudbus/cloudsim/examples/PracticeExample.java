  /**
 * 
 */
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
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterBrokerPractice;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.SJF_DatacenterBroker;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.lists.CloudletList;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

/**
 * @author murad
 *
 */
public class PracticeExample {

	/**
	 * @param args
	 */
	private static List<Cloudlet> cloudeletlist;
	private static List<Cloudlet> cloudeletpacticelist;
	private static List<Cloudlet> cloudeletcpulist;
	private static List<Vm> vmlist;
	static List<Cloudlet> newlist1;
	static long [] joblength = new long[200];
	static int test = 0;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		FileWriter writefile;
		try {
			writefile = new FileWriter("g:\\makespan_time.csv");
			writefile.append("Vm Count");
			writefile.append(",");
			writefile.append("MakeSpan");
			writefile.append("\n");
			
		
		for(int i= 0;i<10;i++) {
			Log.printLine("***************Iteration Number"+i+"************************");
			int user=1;
			Calendar calendar =Calendar.getInstance();
			boolean trace_flag = false;
			CloudSim.init(user, calendar, trace_flag);
			Datacenter datacenter = createdatacenter("Datacenter_0");
			DatacenterBroker datacenterbrooker = createdatacenterbroker();
			DatacenterBrokerPractice practicebroker = createpracticebroker();
			if(test == 0)
			{
			int brokerid = datacenterbrooker.getId();
			vmlist = createVM(brokerid,i);
			cloudeletlist = createCloudlet(brokerid,40);
			datacenterbrooker.submitVmList(vmlist);
			datacenterbrooker.submitCloudletList(cloudeletlist);
				test++;
			}
			if(test==1)
			{
				int practicebrokerid = practicebroker.getId();
				vmlist = createVM(practicebrokerid,1);
				cloudeletpacticelist = practicecreateCloudlet(practicebrokerid,40);
				practicebroker.submitVmList(vmlist);
				practicebroker.submitCloudletList(cloudeletpacticelist); 
				newlist1 = datacenterbrooker.getCloudletReceivedList();
			}
			
			//practice 
			
			CloudSim.startSimulation();
			List<Cloudlet> newlist = datacenterbrooker.getCloudletReceivedList();
			List<Cloudlet> practicelist = practicebroker.getCloudletReceivedList();
			CloudSim.stopSimulation();
			
			writefile.append(Integer.toString(i));
			writefile.append(",");
			//writefile.append(Double.toString(Math.round(newlist.get(newlist.size()-1).getWallClockTime())));
			writefile.append("\n");
			
			 //Checkmethod();
			
			printcloudletlist(newlist);
		}	
		writefile.flush();
		writefile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static DatacenterBrokerPractice createpracticebroker() {
		DatacenterBrokerPractice practicebroker = null;
		
		try {
			practicebroker = new DatacenterBrokerPractice("Broker");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return practicebroker;
	}
	
	private static List<Cloudlet> practicecreateCloudlet(int userid, int cloudlets) {
		// TODO Auto-generated method stub
//		
//		for(int i = 0;i<cloudlets;i++)
//		{
//			Log.printLine("Job " +joblength[i]);
//		}
		
		LinkedList<Cloudlet> cloudlist = new LinkedList<>();
		long fileSize = 300;
		long outputSize = 300;
		int pesNumber = 1;
		UtilizationModelFull utilizationmodel = new UtilizationModelFull();
		Cloudlet[] cloudlet = new Cloudlet[cloudlets];
		
		for(int i = 0;i<cloudlets;i++) {
			Random random = new Random();
			cloudlet[i]= new Cloudlet(i, joblength[i], pesNumber, fileSize,
					outputSize, utilizationmodel, utilizationmodel, 
					utilizationmodel);
			cloudlet[i].setUserId(userid);
			//Log.printLine(cloudlet[i].getActualCPUTime());
			cloudlist.add(cloudlet[i]);
		}
		return cloudlist;
	}

	@SuppressWarnings("null")
	private static void Checkmethod()
	{
		ArrayList<Cloudlet> tempList = new ArrayList<Cloudlet>();
		int count = 0;
		
		for(Cloudlet checkcloudlet: newlist1)
		{
			tempList.add(checkcloudlet);
		}
		Cloudlet cloudlet1, cloudlet2,temp;
		ArrayList<Cloudlet> sortingList= new ArrayList<Cloudlet>();
		int size = tempList.size();
		for(int i = 0;i <size;i++)
		{
			Cloudlet tempcloudlet = tempList.get(0);
			for(Cloudlet checkcloudlet: tempList)
			{
				if(tempcloudlet.getActualCPUTime()>checkcloudlet.getActualCPUTime())
				{
					tempcloudlet = checkcloudlet;
				}
				else if(tempcloudlet.getActualCPUTime()==checkcloudlet.getActualCPUTime())
				{
					if(tempcloudlet.getSubmissionTime()>checkcloudlet.getSubmissionTime())
					{
						tempcloudlet = checkcloudlet;
					}
					else if(tempcloudlet.getSubmissionTime()==checkcloudlet.getSubmissionTime())
					{
						if(tempcloudlet.getCloudletLength()>checkcloudlet.getCloudletLength())
						{
							tempcloudlet = checkcloudlet;
						}
					}
				}
			}
			sortingList.add(tempcloudlet);
			tempList.remove(tempcloudlet);
		}
		for(Cloudlet checkCloudlet: sortingList)
		{
			joblength[count] = checkCloudlet.getCloudletLength();
			count++;
		}
		
		for(int i = 0;i<sortingList.size();i++)
		{
			//Log.printLine(" " +joblength[i]);
		}
		
	}
	
	private static void printcloudletlist(List<Cloudlet> list) {
		// TODO Auto-generated method stub

			int size = list.size();
			Cloudlet cloudlet;

			String indent = "    ";
			Log.printLine();
			Log.printLine("========== OUTPUT ==========");
			Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
					"Data center ID" + indent + "VM ID" + indent + indent + "Flow Time" + indent + "Start Time" 
					+ indent + "Finish Time"+ indent + "Waiting Time"+ indent + "Arrival Time");

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
							indent + indent + indent + dft.format(cloudlet.getWallClockTime()-cloudlet.getActualCPUTime())+
							indent + indent + indent + dft.format(cloudlet.getProcessingCost()));
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
				1999,1888,1777,1166,1555,1444,1333};
		long [] priority_number = {0,1,0,0,1,1};
		long fileSize = 300;
		long outputSize = 300;
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
		int mips = 100;
		long bw = 1000;
		int pesNumber = 1; //number of cpus
		String vmm = "Xen";
		
		Vm[] vm = new Vm[vms];
		for(int i = 0;i<vms;i++) {
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
		
		 List<Host> hostlist= new ArrayList<>();
		 List<Pe> pelist = new ArrayList<>();
		
		 int mips = 1000;
		 
		 PeProvisionerSimple peprovisionersimple = new PeProvisionerSimple(mips);
		 pelist.add(new Pe(0, peprovisionersimple));
		 
		 	int ram = 2048; //host memory (MB)
			long storage = 1000000; //host storage
			int bw = 10000;
			
			Host host = new Host(0, new RamProvisionerSimple(ram), new BwProvisionerSimple(bw),
					storage, pelist, new VmSchedulerSpaceShared(pelist));
			hostlist.add(host);
			
			
			String arch = "x86"; // system architecture
			String os = "Linux"; // operating system
			String vmm = "Xen";
			double time_zone = 10.0; // time zone this resource located
			double cost = 3.0; // the cost of using processing in this resource
			double costPerMem = 0.05; // the cost of using memory in this resource
			double costPerStorage = 0.001; // the cost of using storage in this
											// resource
			double costPerBw = 0.01; 
			
			
			LinkedList<Storage> sanstorage = new LinkedList<>();
			
			DatacenterCharacteristics datacentercharacteristics = new 
					DatacenterCharacteristics(arch, os, vmm, hostlist,time_zone, cost, 
							costPerMem,costPerStorage,costPerBw);
			
			Datacenter datacenter = null;
			
			try {
				datacenter = new Datacenter(name, datacentercharacteristics,new VmAllocationPolicySimple(hostlist), 
						sanstorage,0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return datacenter;
	}

}
