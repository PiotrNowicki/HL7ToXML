package com.piotrnowicki.hl7toxml.bll;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.piotrnowicki.hl7toxml.bll.config.SystemConfig;
import com.piotrnowicki.hl7toxml.model.Segment;

/**
 * Job class which (ran on schedule) which converts input files from specified
 * location to result XML files.
 * 
 * @author Piotr Nowicki
 * 
 */
public class ProcessorJob implements Job {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		XMLHelper xmlHelper;
		try {
			xmlHelper = new XMLHelper();

			// List all *.hl7 files
			WildcardFileFilter fFilter = new WildcardFileFilter("*.hl7");
			List<File> files = (List<File>) FileUtils.listFiles(new File(
					SystemConfig.HL7_INPUT), fFilter, null);

			for (File file : files) {

				// Move the file to new location to check it as parsed
				File fileToProcess = new File(SystemConfig.HL7_PARSED
						+ file.getName());

				FileUtils.moveFile(file, fileToProcess);
				// FileUtils.copyFile(file, fileToProcess);

				List<Segment> segments = xmlHelper.parseMessage(fileToProcess
						.getAbsolutePath());
				xmlHelper.createDocument(fileToProcess.getName() + ".xml",
						segments);
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
