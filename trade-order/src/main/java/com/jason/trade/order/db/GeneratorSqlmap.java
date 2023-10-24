package com.jason.trade.order.db;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GeneratorSqlmap {

    public static void main(String[] args) {
        try {
            // Create an instance of the GeneratorSqlmap class and trigger code generation.
            GeneratorSqlmap generatorSqlmap = new GeneratorSqlmap();
            generatorSqlmap.generateMyBatisFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateMyBatisFiles() throws Exception {
        // Define the path to the MyBatis Generator configuration file.
        File configFile = new File("trade-order/src/main/resources/mybatis-generator-config.xml");

        // Initialize a list to capture any warning messages generated during generation.
        List<String> warningInfos = new ArrayList<String>();

        // Create a callback to handle shell commands.
        DefaultShellCallback callback = new DefaultShellCallback(true);

        // Parse the MyBatis Generator configuration.
        ConfigurationParser configurationParser = new ConfigurationParser(warningInfos);
        Configuration config = configurationParser.parseConfiguration(configFile);

        // Create an instance of MyBatisGenerator and generate code based on the configuration.
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warningInfos);
        myBatisGenerator.generate(null);
    }
}
