package org.cloudbus.cloudsim.examples;

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
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class RR_Experiment {
	private static List<Cloudlet> cloudletlist;
	private static List<Vm> vmlist;
	
	public static void main(String[] args)
	{
		int num_user =2;
		Calendar cal = Calendar.getInstance();
		boolean trac_flag = false;
		
		CloudSim.init(num_user, cal, trac_flag);
		Datacenter datacenter = createdatacenter("Datacenter_0");
		DatacenterBroker datacenterbrorker = createdatacenterbroker();
		int brokerid = datacenterbrorker.getId();
		vmlist = createVm(brokerid,1);
		cloudletlist = createCloudlet(brokerid,10);
		
		datacenterbrorker.submitCloudletList(cloudletlist);
		datacenterbrorker.submitVmList(vmlist);
		
		CloudSim.startSimulation();
		List<Cloudlet> newlist = datacenterbrorker.getCloudletReceivedList();
		CloudSim.stopSimulation();
		printcloudletlist(newlist);
		
	}

	private static void printcloudletlist(List<Cloudlet> newlist) {
		// TODO Auto-generated method stub
		int size = newlist.size();
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
				"Data center ID" + indent + "VM ID" + indent + indent + "Flow Time" + indent + "CPU Time" + indent + "Finish Time"+ indent + "Waiting Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = newlist.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
				Log.print("SUCCESS");

				Log.printLine( indent + indent + cloudlet.getResourceId() + 
						indent + indent + indent + cloudlet.getVmId() +
						indent + indent + indent + dft.format(cloudlet.getActualCPUTime()+cloudlet.getWaitingTime()) +
						indent + indent + dft.format(cloudlet.getActualCPUTime())+ 
						indent + indent + indent + dft.format(cloudlet.getFinishTime())+ 
						indent + indent + indent + dft.format(cloudlet.getWallClockTime()-cloudlet.getActualCPUTime()));
			}
		}	
		
	}

	private static List<Cloudlet> createCloudlet(int brokerid, int i) {
		// TODO Auto-generated method stub
		LinkedList<Cloudlet> cloudlist = new LinkedList<>();
		long length = 1000;
		long filesize = 300;
		long outputfilsize = 300;
		int pesNumber = 1;
		UtilizationModelFull utilizationmodelful= new UtilizationModelFull();
		Cloudlet[] cloudlet = new Cloudlet[i];
		
		for(int j = 0; j<i; j++)
		{
			Random random = new Random();
			cloudlet[j] = new Cloudlet(j,(length+random.nextInt(2000)),pesNumber, filesize,outputfilsize,
					utilizationmodelful,utilizationmodelful,utilizationmodelful);
			cloudlet[j].setUserId(brokerid);
			cloudlist.add(cloudlet[j]);
		}
		return cloudlist;
	}

	private static List<Vm> createVm(int brokerid, int i) {
		// TODO Auto-generated method stub
		LinkedList<Vm> Vmlist = new LinkedList<>();
		
		long size = 10000; //image size (MB)
		int ram = 512; //vm memory (MB)
		int mips = 100;
		long bw = 1000;
		int pesNumber = 1; //number of cpus
		String vmm = "Xen";
		
		Vm[] vm = new Vm[i];
		
		for(int j = 0;j<i;j++)
		{
			vm[j] = new Vm(j, brokerid, mips, pesNumber, ram, bw, size, vmm,
					new CloudletSchedulerTimeShared());
			Vmlist.add(vm[j]);
		}
		return Vmlist;
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
		List<Host> hostlist = new ArrayList<>();
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
			double costPerBw = 0.0; 
			
			
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
