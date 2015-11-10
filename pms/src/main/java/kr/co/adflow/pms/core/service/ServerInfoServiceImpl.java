package kr.co.adflow.pms.core.service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.net.InetAddress;
import java.util.ArrayList;

import kr.co.adflow.pms.domain.CPU;
import kr.co.adflow.pms.domain.Disk;
import kr.co.adflow.pms.domain.Heap;
import kr.co.adflow.pms.domain.Memory;
import kr.co.adflow.pms.domain.ServerInfo;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NfsFileSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ServerInfoServiceImpl implements ServerInfoService {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(ServerInfoServiceImpl.class);

	/** The Constant sigar. */
	private static final Sigar sigar = new Sigar();

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.ServerDAO#get()
	 */
	@Override
	public ServerInfo getServerInfo() throws Exception {
		logger.debug("get시작()");
		// ServerInfo res = new ServerInfo(mqttService.isConnected(),
		// mqttService.getErrorMsg());
		ServerInfo res = new ServerInfo();

		// 서버정보 가져오기
		// CpuInfo[] infos = sigar.getCpuInfoList();
		// CpuPerc[] cpus = sigar.getCpuPercList();
		//
		// org.hyperic.sigar.CpuInfo info = infos[0];
		// long cacheSize = info.getCacheSize();
		// System.out.println("Vendor........." + info.getVendor());
		// System.out.println("Model.........." + info.getModel());
		// System.out.println("Mhz............" + info.getMhz());
		// System.out.println("Total CPUs....." + info.getTotalCores());
		// if ((info.getTotalCores() != info.getTotalSockets())
		// || (info.getCoresPerSocket() > info.getTotalCores())) {
		// System.out.println("Physical CPUs.." + info.getTotalSockets());
		// System.out.println("Cores per CPU.." + info.getCoresPerSocket());
		// }
		//
		// if (cacheSize != Sigar.FIELD_NOTIMPL) {
		// System.out.println("Cache size...." + cacheSize);
		// }

		// for (int i = 0; i < cpus.length; i++) {
		// System.out.println("CPU " + i + ".........");
		// output(cpus[i]);
		// }

		// logger.debug("Totals........");
		// output(sigar.getCpuPerc());

		// get CPU
		CpuPerc cpu = sigar.getCpuPerc();
		// logCPUInfo(cpu);

		CPU cpuInfo = new CPU(cpu.getUser(), cpu.getSys(), cpu.getIdle(),
				cpu.getWait(), cpu.getNice(), cpu.getCombined(), cpu.getIrq());
		if (SigarLoader.IS_LINUX) {
			cpuInfo.setSoftIrq(cpu.getSoftIrq());
			cpuInfo.setStolen(cpu.getStolen());
		}
		logger.debug("cpuInfo= {}", cpuInfo);
		res.setCpu(cpuInfo);

		// System.out.println("**************************************");
		// System.out.println("*** Informations about the Memory: ***");
		// System.out.println("**************************************\n");
		//
		// Mem mem = null;
		// try {
		// mem = sigar.getMem();
		// } catch (SigarException se) {
		// se.printStackTrace();
		// }
		//
		// System.out.println("Actual total free system memory: "
		// + mem.getActualFree() / 1024 / 1024+ " MB");
		// System.out.println("Actual total used system memory: "
		// + mem.getActualUsed() / 1024 / 1024 + " MB");
		// System.out.println("Total free system memory ......: " +
		// mem.getFree()
		// / 1024 / 1024+ " MB");
		// System.out.println("System Random Access Memory....: " + mem.getRam()
		// + " MB");
		// System.out.println("Total system memory............: " +
		// mem.getTotal()
		// / 1024 / 1024+ " MB");
		// System.out.println("Total used system memory.......: " +
		// mem.getUsed()
		// / 1024 / 1024+ " MB");
		//
		// System.out.println("\n**************************************\n");

		// get memory
		Mem mem = sigar.getMem();
		Memory memory = new Memory(mem.getActualFree() / 1024 / 1024,
				mem.getActualUsed() / 1024 / 1024, mem.getFree() / 1024 / 1024,
				mem.getRam(), mem.getTotal() / 1024 / 1024,
				mem.getUsed() / 1024 / 1024);
		logger.debug("memory= {}", memory);
		res.setMemory(memory);

		// Retrieve memory managed bean from management factory.
		MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
		MemoryUsage heap = memBean.getHeapMemoryUsage();
		MemoryUsage nonHeap = memBean.getNonHeapMemoryUsage();

		// Retrieve the four values stored within MemoryUsage:
		// init: Amount of memory in bytes that the JVM initially requests from
		// the OS.
		// used: Amount of memory used.
		// committed: Amount of memory that is committed for the JVM to use.
		// max: Maximum amount of memory that can be used for memory management.
		// System.err.println(String.format(
		// "Heap: Init: %d, Used: %d, Committed: %d, Max.: %d",
		// heap.getInit(), heap.getUsed(), heap.getCommitted(),
		// heap.getMax()));
		// System.err.println(String.format(
		// "Non-Heap: Init: %d, Used: %d, Committed: %d, Max.: %d",
		// nonHeap.getInit(), nonHeap.getUsed(), nonHeap.getCommitted(),
		// nonHeap.getMax()));

		Heap heapInfo = new Heap(heap.getInit() / 1024 / 1024,
				heap.getUsed() / 1024 / 1024,
				heap.getCommitted() / 1024 / 1024, heap.getMax() / 1024 / 1024,
				nonHeap.getInit() / 1024 / 1024,
				nonHeap.getUsed() / 1024 / 1024,
				nonHeap.getCommitted() / 1024 / 1024,
				nonHeap.getMax() / 1024 / 1024);
		logger.debug("heapInfo= {}", heapInfo);
		res.setHeap(heapInfo);

		// get disk info
		ArrayList sys = new ArrayList();
		FileSystem[] fslist = sigar.getFileSystemList();
		for (int i = 0; i < fslist.length; i++) {
			sys.add(fslist[i]);
		}

		Disk[] disk = new Disk[sys.size()];
		for (int i = 0; i < sys.size(); i++) {
			disk[i] = makeDiskInfo((FileSystem) sys.get(i));
			logger.debug("disk[ {} ]= {}", i, disk[i]);
		}

		res.setDisk(disk);

		// get tps
		// double tps = mqttService.getTps();
		// res.setTps(tps);
		// logger.debug("tps=" + tps);

		// logger.debug("canonicalHostName= {}", InetAddress.getLocalHost()
		// .getCanonicalHostName());
		//
		// res.setHostName(InetAddress.getLocalHost().getHostName());
		// res.setIpAddress(InetAddress.getLocalHost().getHostAddress());

		logger.debug("get종료()");
		return res;
	}

	/**
	 * Log cpu info.
	 * 
	 * @param cpu
	 *            the cpu
	 */
	private void logCPUInfo(CpuPerc cpu) {
		logger.debug("UserTime=" + CpuPerc.format(cpu.getUser()));
		logger.debug("SysTime=" + CpuPerc.format(cpu.getSys()));
		logger.debug("IdleTime=" + CpuPerc.format(cpu.getIdle()));
		logger.debug("WaitTime=" + CpuPerc.format(cpu.getWait()));
		logger.debug("NiceTime=" + CpuPerc.format(cpu.getNice()));
		logger.debug("Combined=" + CpuPerc.format(cpu.getCombined()));
		logger.debug("IrqTime=" + CpuPerc.format(cpu.getIrq()));
		if (SigarLoader.IS_LINUX) {
			logger.debug("SoftIrqTime=" + CpuPerc.format(cpu.getSoftIrq()));
			logger.debug("StolenTime=" + CpuPerc.format(cpu.getStolen()));
		}
	}

	/**
	 * Make disk info.
	 * 
	 * @param fs
	 *            the fs
	 * @return the disk
	 */
	public Disk makeDiskInfo(FileSystem fs) {
		long used, avail, total, pct;

		try {
			FileSystemUsage usage;
			if (fs instanceof NfsFileSystem) {
				NfsFileSystem nfs = (NfsFileSystem) fs;
				if (!nfs.ping()) {
					logger.debug("getUnreachableMessage="
							+ nfs.getUnreachableMessage());
					return null;
				}
			}
			usage = this.sigar.getFileSystemUsage(fs.getDirName());

			used = usage.getTotal() - usage.getFree();
			avail = usage.getAvail();
			total = usage.getTotal();

			pct = (long) (usage.getUsePercent() * 100);

		} catch (SigarException e) {
			// e.g. on win32 D:\ fails with "Device not ready"
			// if there is no cd in the drive.
			used = avail = total = pct = 0;
		}

		String usePct;
		if (pct == 0) {
			usePct = "-";
		} else {
			usePct = pct + "%";
		}

		// ArrayList items = new ArrayList();
		//
		// items.add(fs.getDevName());
		// items.add(formatSize(total));
		// items.add(formatSize(used));
		// items.add(formatSize(avail));
		// items.add(usePct);
		// items.add(fs.getDirName());
		// items.add(fs.getSysTypeName() + "/" + fs.getTypeName());

		Disk disk = new Disk();
		disk.setFileSystem(fs.getDevName());
		disk.setSize(formatSize(total));
		disk.setUsed(formatSize(used));
		disk.setAvail(formatSize(avail));
		disk.setUsePct(usePct);
		disk.setMounted(fs.getDirName());
		disk.setType(fs.getSysTypeName() + "/" + fs.getTypeName());

		// System.out.println("disk=" + disk);
		return disk;
	}

	/**
	 * Format size.
	 * 
	 * @param size
	 *            the size
	 * @return the string
	 */
	private String formatSize(long size) {
		return Sigar.formatSize(size * 1024);
	}

}
