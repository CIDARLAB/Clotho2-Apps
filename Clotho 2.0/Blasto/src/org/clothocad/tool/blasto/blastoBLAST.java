/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.tool.blasto;

/**
 *
 * @author cassie
 */
//for running blastn and blast2seq
import static org.biojava3.ws.alignment.qblast.BlastAlignmentParameterEnum.ENTREZ_QUERY;
import java.io.*;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.biojava3.core.sequence.io.util.IOUtils;
import org.biojava3.ws.alignment.qblast.*;
        
public class blastoBLAST {
    	public static void blastn(String inputSeq, JTextArea blastTextArea) {
                blastTextArea.setText("");
		NCBIQBlastService service = new NCBIQBlastService();
 
		// set alignment options
		NCBIQBlastAlignmentProperties props = new NCBIQBlastAlignmentProperties();
		props.setBlastProgram(BlastProgramEnum.blastn);
		props.setBlastDatabase("nt");
 
		// set output options
		NCBIQBlastOutputProperties outputProps = new NCBIQBlastOutputProperties();
                outputProps.setOutputFormat(BlastOutputFormatEnum.Text);
		// in this example we use default values set by constructor (XML format, pairwise alignment, 100 descriptions and alignments) 
 
		// Example of two possible ways of setting output options
//		outputProps.setAlignmentNumber(200);
//		outputProps.setOutputOption(BlastOutputParameterEnum.ALIGNMENTS, "200");
 
		String rid = null;          // blast request ID
		BufferedReader reader = null;
		try {
			// send blast request and save request id
			rid = service.sendAlignmentRequest(inputSeq, props);
 
			// wait until results become available. Alternatively, one can do other computations/send other alignment requests
			while (!service.isReady(rid)) {
				System.out.println("Waiting for results. Sleeping for 5 seconds");
				Thread.sleep(5000);
			}
 
			// read results when they are ready
			InputStream in = service.getAlignmentResults(rid, outputProps);
			reader = new BufferedReader(new InputStreamReader(in));
 
			// write blast output to specified file
 
			String line;
			while ((line = reader.readLine()) != null) {
				blastTextArea.append(line + System.getProperty("line.separator"));
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			// clean up
			IOUtils.close(reader);
 
			// delete given alignment results from blast server (optional operation)
			service.sendDeleteRequest(rid);
                        blastTextArea.setCaretPosition(0);
		}
	}
        
    	public static void blast2seq(String query, String subject, JTextArea blastTextArea) {
                blastTextArea.setText("");
		bl2seqService service = new bl2seqService();
 
		// set output options
		NCBIQBlastOutputProperties outputProps = new NCBIQBlastOutputProperties();
                outputProps.setOutputFormat(BlastOutputFormatEnum.Text);
		// in this example we use default values set by constructor (XML format, pairwise alignment, 100 descriptions and alignments) 
 
		// Example of two possible ways of setting output options
//		outputProps.setAlignmentNumber(200);
//		outputProps.setOutputOption(BlastOutputParameterEnum.ALIGNMENTS, "200");
 
		String rid = null;          // blast request ID
		BufferedReader reader = null;
		try {
			// send blast request and save request id
			rid = service.sendAlignmentRequest(query, subject);
 
			// wait until results become available. Alternatively, one can do other computations/send other alignment requests
			while (!service.isReady(rid)) {
				System.out.println("Waiting for results. Sleeping for 5 seconds");
				Thread.sleep(5000);
			}
 
			// read results when they are ready
			InputStream in = service.getAlignmentResults(rid, outputProps);
			reader = new BufferedReader(new InputStreamReader(in));
 
			// write blast output to specified file
 
			String line;
			while ((line = reader.readLine()) != null) {
				blastTextArea.append(line + System.getProperty("line.separator"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			// clean up
			IOUtils.close(reader);
 
			// delete given alignment results from blast server (optional operation)
			service.sendDeleteRequest(rid);
                        blastTextArea.setCaretPosition(0);
		}
	}
}
