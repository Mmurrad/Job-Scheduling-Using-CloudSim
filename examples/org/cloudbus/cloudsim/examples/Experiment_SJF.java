package org.cloudbus.cloudsim.examples;

import java.io.FileWriter;
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
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.LJF_DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.SJF_DatacenterBroker;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
public class Experiment_SJF {
	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;
	//private static List<Cloudlet> ljf_cloudletlist;
	static Random random = new Random();
	/** The vmList. */
	private static List<Vm> vmlist;
	//private static List<Vm> ljf_vmlist;
	
	public static void main(String[] args) {
		for(int i=0;i<5;i++)
		{
			try {
				int numUser = 1;
				Calendar cal = Calendar.getInstance();
				boolean traceflag = false;
				CloudSim.init(numUser,cal,traceflag);
			
				Datacenter datacenter = createDatacenter("Datacenter_0");
				SJF_DatacenterBroker sjf_broker  = createBroker("Broker");
				//LJF_DatacenterBroker ljf_broker  = ljf_createBroker("LJF_Broker");
				
				int sjf_broker_id = sjf_broker.getId();
				//int ljf_broker_id = ljf_broker.getId();
				
				vmlist = crateVM(sjf_broker_id,i);
				//ljf_vmlist = ljfcrateVM(ljf_broker_id,5);
				
				cloudletList = createCloudlet(sjf_broker_id, 58);// creating 10 cloudlets
				//ljf_cloudletlist = ljfcreateCloudlet(ljf_broker_id, 100);
				
				sjf_broker.submitVmList(vmlist);
				//ljf_broker.submitVmList(ljf_vmlist);
				
				sjf_broker.submitCloudletList(cloudletList);
				//ljf_broker.submitCloudletList(ljf_cloudletlist);
				
				CloudSim.startSimulation();

				// Final step: Print results when simulation is over
				List<Cloudlet> newList = sjf_broker.getCloudletReceivedList();
				//List<Cloudlet> ljfList = ljf_broker.getCloudletReceivedList();

				CloudSim.stopSimulation();

				printCloudletList(newList);
				//printljfCloudletList(ljfList);

				Log.printLine("Shortest Job First finished!");
			}catch(Exception e) {
				e.printStackTrace();
				Log.printLine("The simulation has been terminated due to an unexpected error");
			}
		}
		
	}
	
	private static void printCloudletList(List<Cloudlet> newList) {
		// TODO Auto-generated method stub
		int size = newList.size();
		Cloudlet cloudlet;
		Log.printLine("********************Sortest Job First***********************");
		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
				"Data center ID" + indent + "VM ID" 
				+ indent + indent + "Time" 
				+ indent + "Start Time" 
				+ indent + "Execution Start Time"
				+ indent + "Finish Time"
				+ indent + "Waiting Time"
				+ indent + "WallClock Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = newList.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
				Log.print("SUCCESS");

				Log.printLine( indent + indent + cloudlet.getResourceId() 
						+ indent + indent + indent + cloudlet.getVmId() +
						indent + indent + indent + dft.format(cloudlet.getActualCPUTime()) +
						indent + indent + dft.format(cloudlet.getExecStartTime())
						+ indent+ indent + indent + indent + dft.format(cloudlet.getSubmissionTime())
						+ indent+ indent + indent + indent + dft.format(cloudlet.getFinishTime())
						+ indent+ indent + indent + indent + dft.format(cloudlet.getWaitingTime())
						+ indent+ indent + indent + indent + dft.format(cloudlet.getWallClockTime()));
			}
		}

	}
	
	private static void printljfCloudletList(List<Cloudlet> newList) {
		// TODO Auto-generated method stub
		int size = newList.size();
		Cloudlet cloudlet;
		Log.printLine("********************Longest Job First***********************");
		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
				"Data center ID" + indent + "VM ID" + indent + indent 
				+ "CPU Time" + indent + "Start Time"
				+ indent + "Execution Start Time"
				+ indent + "Finish Time"
				+ indent + "Waiting Time"
				+ indent + "WallClock Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = newList.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
				Log.print("SUCCESS");

				Log.printLine( indent + indent + cloudlet.getResourceId() 
						+ indent + indent + indent + cloudlet.getVmId() +
						indent + indent + indent + dft.format(cloudlet.getActualCPUTime()) +
						indent + indent + dft.format(cloudlet.getExecStartTime())+ indent
						+ indent+ indent + indent + indent + dft.format(cloudlet.getSubmissionTime())
						+ indent+ indent + indent + dft.format(cloudlet.getFinishTime())
						+ indent+ indent + indent + indent + dft.format(cloudlet.getWaitingTime())
						+ indent+ indent + indent + indent + dft.format(cloudlet.getWallClockTime()));
			}
		}

	}

	private static List<Cloudlet> createCloudlet(int sjf_broker_id, int cloudlets) {
		// TODO Auto-generated method stub
		
		LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();

		//cloudlet parameters
		long[] length = {1064,2996,1068,2977,1083,2967,1089,2961,1101,2957,1104,2955,1109,2899,1120,2895,1132,2860,
				1948,1923,1859,1849,1840,1838,1828,1807,1804,1793,1746,1720,
				1613,1585,1567,1564,1536,1523,1488,1483,1480,1474,1421,1419,1412,1404,
				1363,1359,1344,1317,1316,1302,1253,1237,1229,1213,1193,1180,1114,1109};
		long fileSize = 300;
		long outputSize = 300;
		int pesNumber = 1;
		UtilizationModel utilizationModel = new UtilizationModelFull();

		Cloudlet[] cloudlet = new Cloudlet[cloudlets];

		for(int i=0;i<cloudlets;i++){
			
			cloudlet[i] = new Cloudlet(i, length[i], pesNumber, fileSize, 
					outputSize, utilizationModel, utilizationModel, utilizationModel);
			// setting the owner of these Cloudlets
			cloudlet[i].setUserId(sjf_broker_id);
			list.add(cloudlet[i]);
		}
		
		return list;
	}
	private static List<Cloudlet> ljfcreateCloudlet(int ljf_broker_id, int cloudlets) {
		// TODO Auto-generated method stub
		
		LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();

		//cloudlet parameters
		long[] length = {1064,2996,1068,2977,1083,2967,1089,2961,1101,2957,1104,2955,1109,2899,1120,2895,1132,2860,
				1948,1923,1859,1849,1840,1838,1828,1807,1804,1793,1746,1720,
				1613,1585,1567,1564,1536,1523,1488,1483,1480,1474,1421,1419,1412,1404,
				1363,1359,1344,1317,1316,1302,1253,1237,1229,1213,1193,1180,1114,1109};
		
		long fileSize = 300;
		long outputSize = 300;
		int pesNumber = 1;
		UtilizationModel utilizationModel = new UtilizationModelFull();

		Cloudlet[] cloudlet = new Cloudlet[cloudlets];

		for(int i=0;i<cloudlets;i++){
			Random random = new Random();
			cloudlet[i] = new Cloudlet(i, length[i], pesNumber, 
					fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			// setting the owner of these Cloudlets
			cloudlet[i].setUserId(ljf_broker_id);
			list.add(cloudlet[i]);
		}

		return list;
	}
	private static List<Vm> crateVM(int sjf_broker_id, int vms) {
		// TODO Auto-generated method stub
		LinkedList<Vm> list = new LinkedList<Vm>();

		//VM Parameters
		long size = 10000; //image size (MB)
		int ram = 512; //vm memory (MB)
		int mips = 250;
		long bw = 1000;
		int pesNumber = 1; //number of cpus
		String vmm = "Xen"; //VMM name

		//create VMs
		Vm[] vm = new Vm[vms];

		for(int i=0;i<vms;i++){
			vm[i] = new Vm(i, sjf_broker_id, mips, pesNumber, ram, 
					bw, size, vmm, new CloudletSchedulerTimeShared());
			list.add(vm[i]);
		}

		return list;
		
	}
	
	private static List<Vm> ljfcrateVM(int ljf_broker_id, int vms) {
		// TODO Auto-generated method stub
		LinkedList<Vm> list = new LinkedList<Vm>();

		//VM Parameters
		long size = 10000; //image size (MB)
		int ram = 512; //vm memory (MB)
		int mips = 250;
		long bw = 1000;
		int pesNumber = 1; //number of cpus
		String vmm = "Xen"; //VMM name

		//create VMs
		Vm[] vm = new Vm[vms];

		for(int i=0;i<vms;i++){
			vm[i] = new Vm(i, ljf_broker_id, mips, pesNumber, 
					ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			list.add(vm[i]);
		}

		return list;
		
	}
	
	private static SJF_DatacenterBroker createBroker(String name) {
		// TODO Auto-generated method stub
		SJF_DatacenterBroker sjf_broker = null;
		try {
			sjf_broker = new SJF_DatacenterBroker(name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sjf_broker;
	}
	
	private static LJF_DatacenterBroker ljf_createBroker(String name) {
		// TODO Auto-generated method stub
		LJF_DatacenterBroker ljf_broker = null;
		try {
			ljf_broker = new LJF_DatacenterBroker(name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ljf_broker;
	}
	
	
	private static Datacenter createDatacenter(String name) {
		// TODO Auto-generated method stub
		List<Host> hostlist = new ArrayList<Host>();
		
		List<Pe> pelist1 = new ArrayList<Pe>();
		List<Pe> pelist2 = new ArrayList<Pe>();
		
		int mips = 1000;
		
		pelist1.add(new Pe(0, new PeProvisionerSimple(mips)));
		pelist1.add(new Pe(1, new PeProvisionerSimple(mips)));
		pelist1.add(new Pe(2, new PeProvisionerSimple(mips)));
		
		pelist2.add(new Pe(0, new PeProvisionerSimple(mips)));
		pelist2.add(new Pe(1, new PeProvisionerSimple(mips)));
		pelist2.add(new Pe(2, new PeProvisionerSimple(mips)));
		
		int hostId=0;
		int ram = 20000; //host memory (MB)
		long storage = 1000000; //host storage
		int bw = 10000;

		hostlist.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				pelist1,
    				new VmSchedulerSpaceShared(pelist1)
    			)
    		); 
		
		hostId++;
		
		hostlist.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				pelist2,
    				new VmSchedulerSpaceShared(pelist2)
    			)
    		);
		String arch = "x86";      // system architecture
		String os = "Linux";          // operating system
		String vmm = "Xen";
		double time_zone = 10.0;         // time zone this resource located
		double cost = 3.0;              // the cost of using processing in this resource
		double costPerMem = 0.05;		// the cost of using memory in this resource
		double costPerStorage = 0.1;	// the cost of using storage in this resource
		double costPerBw = 0.1;	
		
		LinkedList<Storage> storageList= new LinkedList<Storage>();
		
		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostlist, time_zone, cost, costPerMem, costPerStorage, costPerBw);

		Datacenter datacenter = null;
		
		try {
			datacenter = new Datacenter(name,characteristics,new VmAllocationPolicySimple(hostlist), storageList, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datacenter;
	}
}
