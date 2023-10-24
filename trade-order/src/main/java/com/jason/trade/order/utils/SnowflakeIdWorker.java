package com.jason.trade.order.utils;

public class SnowflakeIdWorker {

    // Constants for bit manipulation

    // The epoch timestamp when the Snowflake algorithm starts
    private final long twepoch = 1640966400000L;

    // Number of bits used for worker ID and data center ID
    private final long workerIdBits = 5L;
    private final long datacenterIdBits = 5L;

    // Maximum worker ID and data center ID that can be used
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    // Number of bits used for the sequence number within a millisecond
    private final long sequenceBits = 12L;

    // Bit shifts for various components of the ID
    private final long workerIdShift = sequenceBits;
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    // A mask to limit the sequence number to its bit size
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    // Worker ID and data center ID
    private long workerId;
    private long datacenterId;

    // Current sequence number and last generated timestamp
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    // Constructor for SnowflakeIdWorker
    public SnowflakeIdWorker(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    // Generate the next unique ID
    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            // If the clock moved backwards, throw an exception
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            // If multiple IDs are requested in the same millisecond, increment the sequence number
            sequence = (sequence + 1) & sequenceMask;

            if (sequence == 0) {
                // If the sequence number overflows, wait for the next millisecond
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // Reset the sequence number if a new millisecond is reached
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        // Combine the components to form the unique ID
        return ((timestamp - twepoch) << timestampLeftShift) //
                | (datacenterId << datacenterIdShift) //
                | (workerId << workerIdShift) //
                | sequence;
    }

    // Wait until the next millisecond to generate an ID
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    // Generate the current timestamp in milliseconds
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    // Main method for testing the SnowflakeIdWorker
    public static void main(String[] args) {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(26, 12);
        for (int i = 0; i < 1000; i++) {
            long id = idWorker.nextId();
            System.out.println(id);
        }
    }
}
