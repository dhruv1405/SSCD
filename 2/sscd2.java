import java.io.*;
import java.util.*;

public class sscd2 {
    public static void main(String[] args) throws IOException {
        BufferedReader icReader = new BufferedReader(new FileReader("IC.txt"));
        BufferedReader symReader = new BufferedReader(new FileReader("symtab.txt"));
        BufferedWriter mcWriter = new BufferedWriter(new FileWriter("sscd2.txt"));
        
        Hashtable<Integer, Integer> symAddress = new Hashtable<>();
        Hashtable<Integer, Integer> symLength = new Hashtable<>();
        
        // Read SYMTAB and store symbol addresses
        String symLine;
        int index = 1;
        while ((symLine = symReader.readLine()) != null) {
            String[] tokens = symLine.split("\t");
            if (tokens.length >= 2) {
                symAddress.put(index, Integer.parseInt(tokens[1]));
                symLength.put(index, Integer.parseInt(tokens[2]));
                index++;
            }
        }
        symReader.close();
        
        // Read IC and generate Machine Code
        String icLine;
        while ((icLine = icReader.readLine()) != null) {
            String[] tokens = icLine.split("\t");
            if (tokens.length < 2) continue;
            
            String lc = tokens[0];
            String opcodePart = tokens[1];
            String operand1 = tokens.length > 2 ? tokens[2] : "";
            String operand2 = tokens.length > 3 ? tokens[3] : "";
            
            String machineCode = "";
            
            if (opcodePart.startsWith("IS")) {
                String opcode = opcodePart.split(",")[1];
                String reg = operand1.equals("-") ? "0" : operand1;
                
                if (operand2.startsWith("S")) {
                    int symIndex = Integer.parseInt(operand2.split(",")[1]);
                    machineCode = lc + " " + opcode + " " + reg + " " + symAddress.get(symIndex);
                } else if (operand2.startsWith("L")) {
                    machineCode = lc + " " + opcode + " " + reg + " " + operand2.split(",")[1];
                } else {
                    machineCode = lc + " " + opcode + " " + reg + " 0";
                }
            } else if (opcodePart.startsWith("DL")) {
                if (operand1.startsWith("C")) {
                    machineCode = lc + " 00 0 " + operand1.split(",")[1];
                }
            }
            
            if (!machineCode.isEmpty()) {
                mcWriter.write(machineCode + "\n");
            }
        }
        
        icReader.close();
        mcWriter.close();
        
        System.out.println("Machine Code has been written to sscd2.txt");
    }
}
