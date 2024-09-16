import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class App {
    static char emptyNodeChar = '|';
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[32m";

    public static void main(String[] args) throws Exception {
        String plaintext = "encrypt me please. I have always wanted to be encrypted since the day that i was made."
                + "Using huffman compression you can make me easier to store by reducing my file size. this quick application "
                + "will showcase the use of huffman compression by reducing this string you are reading now down to a smaller size."
                + " huffman compression works by creating a binary tree called a huffman tree based on the frequency of characters in a string,"
                + " then the tree is used to encode each character of the string as what is basically a path to read the encoded "
                + "character on the huffman tree. now that I have told you how huffman compression works, apply your knowledge now by "
                + "compressing this string.";
        // plaintext = "aabcccddddd";
        Node root = generateHuffmanTree(plaintext);
        ArrayList<Boolean> compressedText = compressTextUsingHuffmanTree(root, plaintext);
        int originalTextSize = plaintext.length() * 16;
        int compressedTextSize = compressedText.size();
        int binaryTreeSize = calculateSizeOfHuffmanTree(root);
        String compressedTextAsString = getStringOfBooleanList(compressedText);
        String uncompressedText = ReadHuffmanCompressedString(root, compressedText);
        float compressionPercentage = 100f * (binaryTreeSize + compressedTextSize) / originalTextSize;

        System.out.print("compressed data as a percentage of uncompressed data: ");
        printInColour(compressionPercentage + "%", ANSI_CYAN);
        System.out.println();
        System.out.print("input text was:\n");
        printInColour(plaintext, ANSI_CYAN);
        System.out.println();
        System.out.print("uncompressed input text was:\n");
        printInColour(uncompressedText, ANSI_CYAN);
        System.out.println();
        System.out.print("compressed data:\n");
        printInColour(compressedTextAsString, ANSI_CYAN);
    }

    public static void printInColour(String text, String colour) {
        System.out.print(colour + text + ANSI_RESET);
    }

    public static String getStringOfBooleanList(ArrayList<Boolean> list) {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i)) {
                result += '1';
            } else {
                result += '0';
            }
        }
        return result;
    }

    public static int calculateSizeOfHuffmanTree(Node root) {
        return calculateNumberOfLeafNodes(root) * 16;
    }

    public static int calculateNumberOfLeafNodes(Node root) {
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right == null) {
            return 1;
        }
        return calculateNumberOfLeafNodes(root.left) + calculateNumberOfLeafNodes(root.right);
    }

    public static String ReadHuffmanCompressedString(Node root, ArrayList<Boolean> paths) {
        String text = "";
        Node head = root;

        for (int i = 0; i < paths.size(); i++) {
            if (paths.get(i)) {
                head = head.left;
            } else {
                head = head.right;
            }
            if (head.value != emptyNodeChar) {
                text += head.value;
                head = root;
            }
        }
        return text;
    }

    public static ArrayList<Boolean> compressTextUsingHuffmanTree(Node root, String text) {
        ArrayList<Boolean> compressedText = new ArrayList<Boolean>();
        ArrayList<Boolean> pathToLetter = new ArrayList<Boolean>();
        for (int i = 0; i < text.length(); i++) {
            findPathToCharacter(root, text.charAt(i), pathToLetter);
            compressedText.addAll(pathToLetter);
            pathToLetter = new ArrayList<Boolean>();
        }
        return compressedText;
    }

    // left on the tree is equal to true
    public static boolean findPathToCharacter(Node node, char target, ArrayList<Boolean> path) {
        if (node == null) {
            return false;
        }
        if (node.value == target) {
            return true;
        }
        boolean targetIsOnLeft = findPathToCharacter(node.left, target, path);
        boolean targetIsOnRight = findPathToCharacter(node.right, target, path);
        if (targetIsOnLeft) {
            path.add(0, true);
            return true;
        } else if (targetIsOnRight) {
            path.add(0, false);
            return true;
        }
        return false;
    }

    // huffman tree generation works
    public static Node generateHuffmanTree(String text) {
        HashMap<Character, Integer> letterFrequency = new HashMap<Character, Integer>();
        ArrayList<Node> nodes = new ArrayList<Node>();
        ArrayList<Integer> frequncies = new ArrayList<Integer>();

        for (int i = 0; i < text.length(); i++) {
            letterFrequency.merge(text.charAt(i), 1, Integer::sum);
        }

        for (Map.Entry<Character, Integer> entry : letterFrequency.entrySet()) {
            // list of nodes must be sorted lowest frequency to highest
            Node tempNode = new Node(entry.getKey(), null, null);
            int frequency = entry.getValue();
            if (nodes.size() == 0) {
                nodes.add(tempNode);
                frequncies.add(frequency);
                continue;
            }
            for (int i = 0; i <= nodes.size(); i++) {
                if (i == nodes.size() || frequncies.get(i) > frequency) {
                    nodes.add(i, tempNode);
                    frequncies.add(i, frequency);
                    break;
                }
            }
        }

        while (nodes.size() > 1) {
            Node combinedNode = new Node(emptyNodeChar, nodes.remove(0), nodes.remove(0));
            int frequencySum = frequncies.remove(0) + frequncies.remove(0);

            for (int i = 0; i <= nodes.size(); i++) {
                if (i == nodes.size() || frequncies.get(i) > frequencySum) {
                    frequncies.add(i, frequencySum);
                    nodes.add(i, combinedNode);
                    break;
                }
            }
        }
        return nodes.get(0);
    }
}
