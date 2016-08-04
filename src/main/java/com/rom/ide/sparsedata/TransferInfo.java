package com.rom.ide.sparsedata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kiva
 */

public class TransferInfo {
    private static final String ERASE = "erase";
    private static final String NEW  = "new";
    private static final String ZERO = "zero";

    public int version;
    public int count;
    public List<TransferCommand> commands;

    public TransferInfo() {
        commands = new ArrayList<>(3);
    }
    
    public int getMaxBlock() {
        int max = 0;
        for (TransferCommand command : commands) {
            for (int element : command.blocks) {
                if (element > max) {
                    max = element;
                }
            }
        }
        return max;
    }

    public static TransferInfo from(String file) throws IOException {
        return from(new File(file));
    }

    public static TransferInfo from(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        TransferInfo info = from(reader);
        reader.close();
        return info;
    }

    public static TransferInfo from(BufferedReader reader)
            throws IOException, IllegalStateException {
        TransferInfo info = new TransferInfo();

        /**
         * 第一行是文件版本
         * 第二行是一共有多少个块
         */
        info.version = Integer.parseInt(reader.readLine());
        info.count = Integer.parseInt(reader.readLine());

        String line;
        int index;
        while ((line = reader.readLine()) != null) {
            /**
             * 可能文件中含有其它数据,但不是我们需要的
             * 我们直接忽略它
             */
            if ((index = line.indexOf(ERASE)) != -1) {
                String eraseArgument = line.substring(index + ERASE.length() + 1);
                TransferCommand command = TransferCommand.make(ERASE, eraseArgument.split(","));
                info.commands.add(command);

            } else if ((index = line.indexOf(NEW)) != -1) {
                String newArgument = line.substring(index + NEW.length() + 1);
                TransferCommand command = TransferCommand.make(NEW, newArgument.split(","));
                info.commands.add(command);

            } else if ((index = line.indexOf(ZERO)) != -1) {
                String zeroArgument = line.substring(index + ZERO.length() + 1);
                TransferCommand command = TransferCommand.make(ZERO, zeroArgument.split(","));
                info.commands.add(command);
            }
        }

        return info;
    }
}
