import utility.CommandReader;
import utility.Console;
import utility.RequestHandler;
import utility.TextFormatting;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println(getEntryInformation());

            while (true) {

                Console.getInstance().setScanner(scanner);

                getRequestHandlerProperties(scanner, InetAddress.getLocalHost());
                RequestHandler.getInstance().setSocketStatus(true);
                System.out.println(RequestHandler.getInstance().getInformation());

                CommandReader commandReader = new CommandReader();
                if (commandReader.enable()) return;
            }

        } catch (IOException e) {
            System.out.println(TextFormatting.getRedText("\nYour computer has problems with the network, " +
                    "run the application again!"));
            return;
        }
    }

    private static String getEntryInformation() {
        return TextFormatting.getGreenText("\n\t\t\t\u0020\u0020\u0020----------------") +
                TextFormatting.getGreenText("\n\t\t\tWelcome to Lab6 Client!") +
                TextFormatting.getGreenText("\n\t\t\t\u0020\u0020------------------\n");
    }

    private static boolean requestTypeOfAddress(Scanner aScanner) {

        String answer;

        do {
            System.out.print(TextFormatting.getGreenText("Do you want to specify the address of the remote host?" +
                    "[\"y\", \"n\"]: "));

            answer = aScanner.nextLine();

        } while (!answer.equals("y") && !answer.equals("n"));

        return answer.equals("y");
    }

    private static int getPort(Scanner scanner) {

        String arg;
        Pattern remoteHostPortPattern = Pattern.compile("^\\s*(\\d{1,5})\\s*");

        do {
            System.out.print(TextFormatting.getGreenText("\nPlease, enter remote host port(1-65535): "));
            arg = scanner.nextLine();
        } while (!(remoteHostPortPattern.matcher(arg).find() && (Integer.parseInt(arg.trim()) < 65536)
                && (Integer.parseInt(arg.trim()) > 0)));

        return Integer.parseInt(arg.trim());
    }

    private static void getRequestHandlerProperties(Scanner scanner, InetAddress localHostAddress) {

        InetAddress remoteHostAddress = localHostAddress;

        if (requestTypeOfAddress(scanner)) {

            String arg;
            Pattern hostAddress = Pattern.compile("^\\s*([\\w.]+)\\s*");

            do {
                System.out.print("\nPlease, enter remote host address! (Example: 5.18.215.199): ");
                arg = scanner.nextLine();
            }
            while (!hostAddress.matcher(arg).find());

            try {
                remoteHostAddress = InetAddress.getByName(arg.trim());
            } catch (UnknownHostException e) {
                System.out.println(TextFormatting.getRedText(
                        "\nThe program could not find the server by the specified address!\n " +
                        "The default address(localhost) will be used!"));
            }
        }
        RequestHandler.getInstance().setRemoteHostSocketAddress(
                new InetSocketAddress(remoteHostAddress, getPort(scanner)));
    }
}