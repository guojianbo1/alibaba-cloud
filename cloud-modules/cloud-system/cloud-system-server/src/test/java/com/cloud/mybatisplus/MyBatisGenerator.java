package com.cloud.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.mysql.cj.jdbc.Driver;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 代码生成工具类
 *
 * @author guojianbo
 * @date 2023/6/21 16:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MyBatisGenerator {
    static AutoGenerator mpg = null;

    @Value("${spring.datasource.username}")
    String jdbcUsername;
    @Value("${spring.datasource.password}")
    String jdbcPassword;
    @Value("${spring.datasource.url}")
    String jdbcUrl;

    String parent = "com.cloud";
    String module;
    String[] tablePrefix;
    String[] tables;

    public void initTables(){
        tablePrefix = new String[] {"ry"};
        tables = new String[] {"ry_user_balance_flow"};
    }

    /**
     * 新增模块
     */
    @Test
    public void newModule() {
        initAutoGenerator();
        mpg.getGlobalConfig().setFileOverride(false);

        TemplateConfig tc = new TemplateConfig();
        // 设置不生成controller
//        tc.setController(null);
        mpg.setTemplate(tc);
        mpg.execute();
    }

    /**
     * 更新实体
     */
    @Test
    public void updateEntity() throws InterruptedException {
        int i = 0;

        do {
            System.err.println("!!!更新实体会覆盖实体类，请注意合并实体类代码!!!");
            i++;
            Thread.sleep(1000L);
        }while (i < 5);

        for (String t : tables){
            // fix 缓存
            initAutoGenerator();

            mpg.getGlobalConfig().setFileOverride(true);
            mpg.getStrategy().setInclude(t);

            // 只更新entity
            TemplateConfig tc = new TemplateConfig();
            tc.setMapper(null);
            tc.setXml(null);
            tc.setService(null);
            tc.setServiceImpl(null);
            tc.setController(null);

            mpg.setTemplate(tc);
            mpg.execute();
        }
    }

    @Before
    public void init() {
        initTables();
    }

    void initAutoGenerator(){
        mpg = new AutoGenerator();

        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        mpg.setCfg(cfg);

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        // 配置主键生成策略，此处为 ASSIGN_ID（可选）
        gc.setIdType(IdType.ASSIGN_ID);
        // 拼接出代码最终输出的目录
        gc.setOutputDir(System.getProperty("user.dir") + "/src/main/java");
        // 重新生成文件时是否覆盖，false 表示不覆盖（可选）
        gc.setFileOverride(true);
        //是否支持AR模式
        gc.setActiveRecord(false);
        //是否开启缓存
        gc.setEnableCache(false);
        //生成resultMap
        gc.setBaseResultMap(true);
        //在xml中生成基础列
        gc.setBaseColumnList(true);
        //设置作者
        gc.setAuthor("guojianbo");
        // 配置是否打开目录，false 为不打开（可选）
        gc.setOpen(false);
        // 是否开启swagger
        gc.setSwagger2(false);

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setEntityName("%s");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");

        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName(Driver.class.getName());
        dsc.setUsername(jdbcUsername);
        dsc.setPassword(jdbcPassword);
        dsc.setUrl(jdbcUrl);
        MySqlTypeConvert typeConvert = new MySqlTypeConvert() {
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                if (fieldType.toLowerCase().contains("timestamp")) {
                    // 将timestamp转换为java.util.Date
                    return DbColumnType.DATE;
                }
                return super.processTypeConvert(globalConfig, fieldType);
            }
        };
        dsc.setTypeConvert(typeConvert);
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setTablePrefix(tablePrefix);
        // 表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 需要生成的表
        strategy.setInclude(tables);
        // 设置lombok
        strategy.setEntityLombokModel(true);
        // 【实体】是否生成字段常量（默认 false）
        strategy.setEntityColumnConstant(true);
        strategy.setEntityBooleanColumnRemoveIsPrefix(false);
        // 【实体】是否为构建者模型（默认 false）
        strategy.setEntityBuilderModel(true);
        // 设置乐观锁字段
        strategy.setVersionFieldName("version");
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setEntity("entity");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setController("controller");
        pc.setParent(parent);
        pc.setModuleName(module);
        mpg.setPackageInfo(pc);

        // 关闭默认生成
        // TemplateConfig tc = new TemplateConfig();
        // tc.setController(null);
        // tc.setService(null);
        // tc.setServiceImpl(null);
        // mpg.setTemplate(tc);
    }
}
