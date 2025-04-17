import java.io.*;
import java.util.*;

public class sscd1 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("sscd1input.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("IC.txt"));

        int LC = 0;
        String instropcode = null;

        Hashtable<String, String> is = new Hashtable<>();
        is.put("STOP", "00");
        is.put("ADD", "01");
        is.put("SUB", "02");
        is.put("MULT", "03");
        is.put("MOVER", "04");
        is.put("MOVEM", "05");
        is.put("COMP", "06");
        is.put("BC", "07");
        is.put("DIV", "08");
        is.put("READ", "09");
        is.put("PRINT", "10");

        Hashtable<String, String> ad = new Hashtable<>();
        ad.put("START", "01");
        ad.put("END", "02");
        ad.put("ORIGIN", "03");
        ad.put("EQU", "04");
        ad.put("LTORG", "05");

        Hashtable<String, String> dl = new Hashtable<>();
        dl.put("DC", "01");
        dl.put("DS", "02");

        Hashtable<String, Integer> symAddress = new Hashtable<>();
        Hashtable<String, Integer> symLength = new Hashtable<>();
        Hashtable<String, Boolean> symDefined = new Hashtable<>();
        List<String> symList = new ArrayList<>();

        String icCode = "";
        String line;

        while ((line = br.readLine()) != null) {
            String[] tokens = line.trim().split("\\s+");
            if (tokens.length == 0 || tokens[0].isEmpty()) {
                continue;
            }

            String label = "";
            String opcode = tokens[0];
            String operand1 = "";
            String operand2 = "";

            // Handle labels correctly
            if (!is.containsKey(opcode) && !ad.containsKey(opcode) && !dl.containsKey(opcode)) {
                label = opcode;
                opcode = tokens.length > 1 ? tokens[1] : "";
                if (tokens.length > 2) {
                    String[] operands = tokens[2].split(",");
                    operand1 = operands.length > 0 ? operands[0].trim() : "";
                    operand2 = operands.length > 1 ? operands[1].trim() : "";
                }
            } else {
                if (tokens.length > 1) {
                    String[] operands = tokens[1].split(",");
                    operand1 = operands.length > 0 ? operands[0].trim() : "";
                    operand2 = operands.length > 1 ? operands[1].trim() : "";
                }
            }

            // Ensure correct handling of labels
            if (!label.isEmpty()) {
                if (!symAddress.containsKey(label)) {
                    symAddress.put(label, LC);
                    symLength.put(label, 1);
                    symDefined.put(label, true);
                    symList.add(label);
                } else if (!symDefined.get(label)) {
                    symAddress.put(label, LC);
                    symDefined.put(label, true);
                }
            }

            if (ad.containsKey(opcode)) {
                instropcode = ad.get(opcode);
                if (opcode.equals("START")) {
                    LC = Integer.parseInt(operand1);
                    icCode = "-\tAD," + instropcode + "\t-\tC," + LC;
                } else if (opcode.equals("END")) {
                    icCode = "-\tAD," + instropcode;
                } else if (opcode.equals("ORIGIN")) {
                    LC = Integer.parseInt(operand1);
                    icCode = "-\tAD," + instropcode + "\tC," + LC;
                }
            } else if (is.containsKey(opcode)) {
                instropcode = is.get(opcode);
                String reg = "0";

                // ✅ Fix register recognition
                operand1 = operand1.toUpperCase();
                if (operand1.equals("AREG")) reg = "1";
                else if (operand1.equals("BREG")) reg = "2";
                else if (operand1.equals("CREG")) reg = "3";

                System.out.println("Parsed Operand1: [" + operand1 + "] -> Register Code: " + reg);

                if (!operand2.isEmpty()) {
                    if (operand2.startsWith("=")) {
                        icCode = LC + "\tIS," + instropcode + "\t" + reg + "\tL," + operand2;
                    } else {
                        if (!symAddress.containsKey(operand2)) {
                            symAddress.put(operand2, 0);
                            symLength.put(operand2, 1);
                            symDefined.put(operand2, false);
                            symList.add(operand2);
                        }
                        int symIndex = symList.indexOf(operand2) + 1;
                        icCode = LC + "\tIS," + instropcode + "\t" + reg + "\tS," + symIndex;
                    }
                } else {
                    icCode = LC + "\tIS," + instropcode + "\t" + reg + "\t-";
                }
                LC++;
            } else if (dl.containsKey(opcode)) {
                instropcode = dl.get(opcode);
                icCode = LC + "\tDL," + instropcode + "\tC," + operand1;

                if (opcode.equals("DS")) {
                    int length = Integer.parseInt(operand1);
                    if (!label.isEmpty()) {
                        symLength.put(label, length);
                        symAddress.put(label, LC);
                    }
                    LC += length;
                } else {
                    LC++;
                }
            }

            if (icCode != null) {
                bw.write(icCode + "\n");
                icCode = null;
            }
        }

        br.close();
        bw.close();

        try (PrintWriter symWriter = new PrintWriter(new FileWriter("symtab.txt"))) {
            for (String symbol : symList) {
                int address = symAddress.get(symbol);
                int length = symLength.getOrDefault(symbol, 1);
                symWriter.println(symbol + "\t" + address + "\t" + length);
            }
            System.out.println("SYMTAB has been written to symtab.txt");
        } catch (IOException e) {
            System.err.println("Error writing SYMTAB to file: " + e.getMessage());
        }
    }
}
