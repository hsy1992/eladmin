package me.zhengjie.res;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhang houying
 * @date 2019-11-03
 */
@RestController
@RequestMapping("/api/serverMonitor")
public class ServerMonitorController {

	private Sigar sigar = new Sigar();
	private static final double GB = 1024*1024*1024.00;

	@GetMapping
    public ResponseEntity getServerInfo(){
    	Map<String,Object> resultMap = new HashMap<>(8);
		try {
			CpuInfo[] infoList = sigar.getCpuInfoList();
			resultMap.put("cpuRate",sigar.getCpuPerc().getCombined());
			resultMap.put("cpuCore",infoList.length);
			resultMap.put("memTotal",sigar.getMem().getTotal()/GB);
			resultMap.put("memUsed",sigar.getMem().getUsed()/GB);
			FileSystem[] fsArray = sigar.getFileSystemList();
			double diskTotal = 0;
			double diskUsed = 0;
			for (int i = 0; i < fsArray.length; i++) {
				try {
					FileSystem fs = fsArray[i];
					FileSystemUsage usage = null;
					usage = sigar.getFileSystemUsage(fs.getDirName());
					switch (fs.getType()) {
						case 2:
							//本地硬盘
							diskTotal += usage.getTotal()/GB*1024;
							diskUsed += usage.getUsed()/GB*1024;
							break;
					}
				} catch (Exception e) {}
			}
			resultMap.put("diskTotal",diskTotal);
			resultMap.put("diskUsed",diskUsed);
			resultMap.put("swapTotal",sigar.getSwap().getTotal()/GB);
			resultMap.put("swapUsed",sigar.getSwap().getUsed()/GB);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

}