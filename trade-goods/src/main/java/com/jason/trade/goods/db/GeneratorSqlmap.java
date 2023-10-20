package com.jason.trade.goods.db;


import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GeneratorSqlmap {

    public static void main(String[] args) throws Exception {
        try {
            GeneratorSqlmap generatorSqlmap = new GeneratorSqlmap();
            generatorSqlmap.generator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generator() throws Exception {
        File configFile = new File("trade-goods/src/main/resources/mybatis-generator-config.xml");
        List<String> warningInfos = new ArrayList<String>();
        DefaultShellCallback callback = new DefaultShellCallback(true);
        ConfigurationParser configurationParser = new ConfigurationParser(warningInfos);
        Configuration config = configurationParser.parseConfiguration(configFile);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warningInfos);
        myBatisGenerator.generate(null);
    }

}
