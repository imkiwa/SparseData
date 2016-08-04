package com.rom.ide.sparsedata;

/**
 * @author kiva
 */

public class TransferCommand {
    public String command;
    public int[] blocks;

    public TransferCommand() {
    }

    public static TransferCommand make(String command, String[] blockStrings)
        throws IllegalStateException {
        int blockCount = Integer.parseInt(blockStrings[0]);
        if (blockCount != blockStrings.length - 1) {
            throw new IllegalStateException("blockCount doesn't match");
        }

        TransferCommand transferCommand = new TransferCommand();
        transferCommand.command = command;
        transferCommand.blocks = new int[blockStrings.length - 1];

        for (int i = 1; i < blockStrings.length; ++i) {
            transferCommand.blocks[i - 1] = Integer.parseInt(blockStrings[i]);
        }
        return transferCommand;
    }

    public boolean isNewCommand() {
        return "new".equals(command);
    }
}
