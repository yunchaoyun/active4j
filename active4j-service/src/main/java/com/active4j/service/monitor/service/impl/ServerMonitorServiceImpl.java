package com.active4j.service.monitor.service.impl;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.active4j.common.util.ArithUtil;
import com.active4j.common.util.DateUtils;
import com.active4j.monitor.model.CPU;
import com.active4j.monitor.model.FileSys;
import com.active4j.monitor.model.JVM;
import com.active4j.monitor.model.MEM;
import com.active4j.monitor.model.SYS;
import com.active4j.monitor.model.ServerInfoModel;
import com.active4j.service.monitor.service.ServerMonitorService;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

/**
 * 
 * @title ServerMonitorServiceImpl.java
 * @description 
		服务器监控
 * @time  2019年12月31日 下午1:53:41
 * @author guyp
 * @version 1.0
 */
@Service("serverMonitorService")
@Transactional
public class ServerMonitorServiceImpl implements ServerMonitorService {

	/**
	 * 
	 * @description
	 *  	获取服务器信息
	 * @params
	 * @return ServerInfoModel
	 * @author guyp
	 * @time 2019年12月31日 下午1:52:12
	 */
	public ServerInfoModel getServer() {
		// 初始化系统信息
		SystemInfo si = new SystemInfo();
	
		ServerInfoModel server = new ServerInfoModel();
		server.setJvm(getJvm());
		server.setSys(getSys(si));
		server.setLstFileSys(getFilesys(si));
		server.setCpu(getCpu(si));
		server.setMem(getMem(si));
		return server;
	}
	
	/**
	 * 
	 * @description 获取内存信息
	 * @params SystemInfo
	 * @return MEM
	 * @author 麻木神
	 * @time 2019年12月4日 上午11:21:27
	 */
	private MEM getMem(SystemInfo si) {
		MEM mem = new MEM();

		HardwareAbstractionLayer hal = si.getHardware();
		mem.setTotal(hal.getMemory().getTotal());
		mem.setUsed(hal.getMemory().getTotal() - hal.getMemory().getAvailable());
		mem.setFree(hal.getMemory().getAvailable());

		mem.setJvmTotal(Runtime.getRuntime().totalMemory());
        mem.setJvmFree(Runtime.getRuntime().freeMemory());
		
		return mem;
	}

	/**
	 * 
	 * @description 获取CPU信息
	 * @params SystemInfo
	 * @return CPU
	 * @author 麻木神
	 * @time 2019年12月4日 上午10:41:55
	 */
	private CPU getCpu(SystemInfo si) {
		CPU cpu = new CPU();

		HardwareAbstractionLayer hal = si.getHardware();
		// CPU信息
		long[] prevTicks = hal.getProcessor().getSystemCpuLoadTicks();
		Util.sleep(1000);
		long[] ticks = hal.getProcessor().getSystemCpuLoadTicks();
		long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
		long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
		long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
		long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
		long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
		long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
		long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
		long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
		long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
		cpu.setCpuNum(hal.getProcessor().getLogicalProcessorCount());
		cpu.setTotal(totalCpu);
		cpu.setSys(cSys);
		cpu.setUsed(user);
		cpu.setWait(iowait);
		cpu.setFree(idle);

		return cpu;
	}

	/**
	 * 
	 * @description 获取文件系统信息
	 * @params SystemInfo
	 * @return List<FileSys>
	 * @author 麻木神
	 * @time 2019年12月4日 上午9:41:59
	 */
	private List<FileSys> getFilesys(SystemInfo si) {

		List<FileSys> lstFileSys = new ArrayList<FileSys>();

		OperatingSystem os = si.getOperatingSystem();
		FileSystem fileSystem = os.getFileSystem();
		OSFileStore[] fsArray = fileSystem.getFileStores();
		for (OSFileStore fs : fsArray) {
			long free = fs.getUsableSpace();
			long total = fs.getTotalSpace();
			long used = total - free;
			FileSys fileSys = new FileSys();
			fileSys.setDirName(fs.getMount());
			fileSys.setSysTypeName(fs.getType());
			fileSys.setTypeName(fs.getName());
			fileSys.setTotal(convertFileSize(total));
			fileSys.setFree(convertFileSize(free));
			fileSys.setUsed(convertFileSize(used));
			fileSys.setUsage(ArithUtil.mul(ArithUtil.div(used, total, 4), 100));
			lstFileSys.add(fileSys);
		}

		// 排序
		lstFileSys.sort(Comparator.comparing(FileSys::getDirName));

		return lstFileSys;
	}

	/**
	 * @description 获取虚拟机信息
	 * @params
	 * @return JVM
	 * @author 麻木神
	 * @time 2019年12月3日 下午4:40:59
	 */
	private JVM getJvm() {
		JVM jvm = new JVM();
		jvm.setName(ManagementFactory.getRuntimeMXBean().getVmName());
		Properties props = System.getProperties();
		jvm.setVersion(props.getProperty("java.version"));
		jvm.setHome(props.getProperty("java.home"));
		jvm.setUserDir(props.getProperty("user.dir"));

		// 虚拟机运行时间
		long time = ManagementFactory.getRuntimeMXBean().getStartTime();
		jvm.setStartTime(DateUtils.getDate2Str(DateUtils.getDate(time)));
		jvm.setRunTime(DateUtils.getDateDiff(DateUtils.getNow(), DateUtils.getDate(time)));
		return jvm;
	}

	/**
	 * 
	 * @description 获取服务器信息
	 * @return SYS
	 * @author 麻木神
	 * @time 2019年12月3日 下午5:18:00
	 */
	private SYS getSys(SystemInfo si) {
		HardwareAbstractionLayer hal = si.getHardware();
		Properties props = System.getProperties();
		SYS sys = new SYS();
		sys.setCore(String.valueOf(hal.getProcessor().getPhysicalProcessorCount()));
		sys.setCpus(String.valueOf(hal.getProcessor().getLogicalProcessorCount()));
		sys.setName(hal.getComputerSystem().getModel());
		sys.setProductor(hal.getComputerSystem().getManufacturer());
		sys.setOs(props.getProperty("os.name"));
		sys.setStructure(props.getProperty("os.arch"));
		return sys;
	}

	public String convertFileSize(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;
		if (size >= gb) {
			return String.format("%.1f GB", (float) size / gb);
		} else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		} else if (size >= kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		} else {
			return String.format("%d B", size);
		}
	}

}
