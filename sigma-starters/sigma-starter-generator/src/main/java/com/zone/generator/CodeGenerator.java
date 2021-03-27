package com.zone.generator;import com.baomidou.mybatisplus.generator.AutoGenerator;import com.baomidou.mybatisplus.generator.InjectionConfig;import com.baomidou.mybatisplus.generator.config.*;import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;import com.baomidou.mybatisplus.generator.config.rules.FileType;import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;import com.google.common.collect.Lists;import java.io.File;import java.util.Arrays;import java.util.List;/** * @Author: jianyong.zhu * @Date: 2020/8/30 8:47 下午 * @Description: */public class CodeGenerator {    public static void main(String[] args) {        // 数据源配置        String dbUrl = "jdbc:mysql://localhost:3306/sigma_process?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull";        String dbUser = "root";        String dbPass = "12345678";        String dbDriver = "com.mysql.cj.jdbc.Driver";        // 生成文件的路径配置        String project = "sigma-process";        String module = "sigma-services";        String author = "Jone";        // 包名配置        List<String> tables = Arrays.asList("process_category", "process_def", "process_def_node", "process_def_node_variable",                "process_def_node_property", "process_inst", "process_inst_operation", "process_inst_data", "form_structure");        String parentPackage = "com.zone.process";        generateCode(dbUrl, dbUser, dbPass, dbDriver, project, module, author, parentPackage, tables);    }    public static void generateCode(String dbUrl, String dbUser, String dbPass, String dbDriver,                                    String project, String module, String author,                                    String parentPackage,                                    List<String> tables    ) {        // 项目的路径：在哪个module的project下        String projectPath = System.getProperty("user.dir")                + "/"                + module                + "/"                + project;        // 代码生成器        AutoGenerator generator = new AutoGenerator();        /*           全局配置         */        GlobalConfig gc = new GlobalConfig()                // 文件输出路径                .setOutputDir(projectPath + "/src/main/java")                .setAuthor(author)                // 是否打开输出目录                .setOpen(false)                // 实体属性 Swagger2 注解                .setSwagger2(true)                // 是否覆盖已有文件                .setFileOverride(false)                // 生成文件的名称                .setEntityName("%sDO")                .setMapperName("%sMapper")                .setXmlName("%sMapper")                .setServiceName("%sRepository")                .setServiceImplName("%sRepositoryImpl")                .setControllerName("%sController");        generator.setGlobalConfig(gc);        /*           数据源配置         */        DataSourceConfig dsc = new DataSourceConfig();        dsc.setUrl(dbUrl);        dsc.setDriverName(dbDriver);        dsc.setUsername(dbUser);        dsc.setPassword(dbPass);        generator.setDataSource(dsc);        /*           包配置: 设置项目结构         */        PackageConfig pc = new PackageConfig()                .setParent(parentPackage)                .setController("inbound.web.controller")                .setEntity("infrastructure.db.dataobject")                .setMapper("infrastructure.db.mapper")                .setService("infrastructure.db.repository")                .setServiceImpl("infrastructure.db.repository.impl");        generator.setPackageInfo(pc);        /*           默认模板配置，用自定义的模版代替默认模版         */        TemplateConfig templateConfig = new TemplateConfig();        templateConfig.setXml(null);        generator.setTemplate(templateConfig);        /*           自定义模版配置：生成其他的一些自定义文件         */        InjectionConfig cfg = new InjectionConfig() {            @Override            public void initMap() {                // to do nothing            }        };        // 文件创建的配置        cfg.setFileCreate(new IFileCreate() {            @Override            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {                // 判断自定义文件夹是否需要创建                checkDir(filePath);                // 这里通过对文件类型的 if-else 的判断来过滤对已经生成的文件的覆盖//                if (fileType == FileType.MAPPER) {//                    // 已经生成 mapper 文件判断存在，不想重新生成返回 false//                    return !new File(filePath).exists();//                }                // 不生成 service 和 serviceImpl                if (fileType == FileType.SERVICE || fileType == FileType.SERVICE_IMPL) {                    return false;                }                // 允许生成模板文件                return true;            }        });        generator.setCfg(cfg);        /*           策略配置：对生成的DO进行配置，指定要生成哪些表，排除哪些表，命名方式等         */        StrategyConfig strategy = new StrategyConfig()                .setNaming(NamingStrategy.underline_to_camel)                .setColumnNaming(NamingStrategy.underline_to_camel)                .setEntityLombokModel(true)                .setRestControllerStyle(true)                .setInclude(tables.toArray(tables.toArray(new String[tables.size()])));        generator.setStrategy(strategy);        /*           生成文件         */        generator.setTemplateEngine(new FreemarkerTemplateEngine());        generator.execute();        // command application service 的目录结构        String appCommandServiceCmd = "application.service.command.cmd";        // query application service 的目录结构        String appQueryServiceDto = "application.service.query.dto";        String appQueryServiceAssembler = "application.service.query.assembler";        // 领域层的一些目录结构        String domainAgg = "domain.agg";        String domainService = "domain.service";        String domainEvent = "domain.event";        String domainRepository = "domain.repository";        String domainValueObject = "domain.valueobject";        // 程序入口        String inboundJobController = "inbound.job.controller";        String inboundRpcController = "inbound.rpc.controller";        // 基础设施层        String infrastructureClient = "infrastructure.client.impl";        // cmd 和 query 共用的一些类        String sharedConstant = "shared.constants";        String sharedInterfaces = "shared.interfaces";        String sharedUtils = "shared.utils";        String sharedClient = "shared.client";        List<String> routes = Lists.newArrayList(appCommandServiceCmd,                appQueryServiceAssembler, appQueryServiceDto,                domainAgg, domainService, domainEvent, domainRepository, domainValueObject,                inboundJobController, inboundRpcController,                infrastructureClient, sharedClient, sharedConstant, sharedInterfaces, sharedUtils);        // 创建必要的目录        routes.forEach(tmp -> {            String route = projectPath + "/src/main/java/"                    + parentPackage.replace(".", "/")                    + "/"                    + tmp.replace(".", "/");            File file = new File(route);            if (!file.exists()) {                file.mkdirs();            } else {                System.out.println(route + "已存在");            }        });    }}