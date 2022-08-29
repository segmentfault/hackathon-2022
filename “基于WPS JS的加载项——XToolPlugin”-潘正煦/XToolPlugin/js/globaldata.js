//GlobalData
var GlobalData={
    PassWord:"112233",
    JsonName:"GlobalData.json",
    Version:"1.0.1",//用于判断是否替换本地数据
    IsShowImportTestDataBtn:true,
    CFA:{
        SavePath:wps.Env.GetHomePath().concat("/","授权书/"),
        IsClearBeforeCreate:true,
        IsCreateNewContract:false,
        IsAddSign:false,
        TableData:{},
        DefaultData:{}
    },
    SLC:{
        SavePath:wps.Env.GetHomePath().concat("/","服务承诺函/"),
        IsClearBeforeCreate:true,
        IsCreateNewContract:false,
        IsAddSign:false,
        TableData:{},
        DefaultData:{}
        
    },
    Contract:{
        SavePath:wps.Env.GetHomePath().concat("/","合同/"),
        IsClearBeforeCreate:true,
        IsCreateNewContract:false,
        IsTrackRevisions:true,
        LastDate:"",
        Count:0
    },
    Certfication:{
        SavePath:wps.Env.GetHomePath().concat("/","资质/"),
        IsClearBeforeCreate:true,
        IsCreateNewContract:false,
        IsBold:true,
        FontSize:16,
        FontName:"宋体",
        Left:50,
        Top:400,
        Width:400,
        Height:150,
        Data:[
                {CertificationName:"公司资质",CertificationFileName:"公司资质.docx",CertificationInfo:"营业执照、IOS认证、软件企业认定证书",FilePath:GetTemplateFileUrl()},
                {CertificationName:"产品资质",CertificationFileName:"产品资质.docx",CertificationInfo:"国密检测报告、涉密信息系统检测证书、某某产品著作权、某某某某产品著作权",FilePath:GetTemplateFileUrl()}
            ]
    },
    Product:{
        "产品分类": [
        { "产品名称":"煦航论文自动排版软件V1.0","版本ID":1,"授权ID":1,"语言ID":1,"价格ID":1,"数量ID":1,"功能描述":"可以自动生成符合格式要求的论文，支持自动生成中文封面页、英文封面页、中文摘要页、英文摘要页、严正申明页、目录页、论文正文、感谢页、参考文献页等。" },
        { "产品名称":"煦航OFD阅读软件V1.0","版本ID":2,"授权ID":2,"语言ID":2,"价格ID":2,"数量ID":2,"功能描述":"支持OFD/PDF电子文件的阅读浏览、文档操作、图文注释等通用版式处理功能，还根据公务办公特点，提供原笔迹签批、电子印章、语义应用、修订标记等公务应用扩展功能。阅读器体积小巧、高效稳定，支持各类自主可控软硬件环境，为用户提供卓越的电子文件阅读与处理体验。" },
        { "产品名称":"煦航WPS加载项软件V1.0","版本ID":3,"授权ID":3,"语言ID":3,"价格ID":3,"数量ID":3,"功能描述":"WPS 加载项是一套基于 Web 技术用来扩展 WPS 应用程序的解决方案。每个 WPS 加载项都对应打开了一个网页，并通过调用网页中 JavaScript 方法来完成其功能逻辑。 WPS 加载项打开的网页可以直接与 WPS 应用程序进行交互，同时一个 WPS 加载项中的多个网页形成了一个整体， 相互之间可以进行数据共享。" }
        ],
        "产品版本": [
        { "版本名称":"标准版","版本ID":1,"版本描述":"标准版运行环境要求：32位浏览器IE8-11；32位完整版MS OFFICE2003-2016；32位环境下的专业版WPS2007-2016；" },
        { "版本名称":"专业版","版本ID":1,"版本描述":"专业版包含32位控件和64位控件，运行环境要求： 32位控件：32位浏览器IE8-11、chrome28-54、Firefox28-45；32位完整版MS OFFICE2003-2016；32位环境下的专业版WPS2007-2016； 64位控件：64位浏览器IE8-11， 64位完整版MS OFFICE2003-2016；" },
        { "版本名称":"信创版","版本ID":2,"版本描述":"支持在中标麒麟、银河麒麟、中科方德、Uos等国产化操作系统中打开OFD文档。" },
        { "版本名称":"移动版","版本ID":2,"版本描述":"支持在Android6.0以上版本，IOS8.0以上版本中打开OFD文档" },
        { "版本名称":"Windows版","版本ID":2,"版本描述":"支持在xp、win7、win8、win10、win11等windows操作系统中打开OFD文档。" },
        { "版本名称":"Windows版","版本ID":3,"版本描述":"支持在xp、win7、win8、win10、win11等国产化操作系统中生成授权书、服务承诺函等文档。" },
        { "版本名称":"信创版","版本ID":3,"版本描述":"支持在中标麒麟、银河麒麟、中科方德、Uos等国产化操作系统中生成授权书、服务承诺函等文档" }
        ],
        "产品授权": [
        { "授权名称":"单套授权","授权ID":1,"授权描述":"授权甲方给一个最终用户单位内部或者直接授权一个最终用户单位内部系统中使用此“产品”，并不授权其他任何第三方单位使用。产品本身不能单独对外销售。" },
        { "授权名称":"网站授权","授权ID":1,"授权描述":"授权甲方在一个网站上使用此“产品”，并不授权其他任何第三方网站使用。产品本身不能单独对外销售。" },
        { "授权名称":"客户端授权","授权ID":2,"授权描述":"授权甲方在一个终端设备上使用本产品。" },
        { "授权名称":"单套授权","授权ID":2,"授权描述":"授权甲方给一个最终用户单位内部或者直接授权一个最终用户单位内部系统中使用此“产品”，并不授权其他任何第三方单位使用。产品本身不能单独对外销售。" },
        { "授权名称":"网站授权","授权ID":2,"授权描述":"授权甲方在一个网站上使用此“产品”，并不授权其他任何第三方网站使用。产品本身不能单独对外销售。" },
        { "授权名称":"单套授权","授权ID":3,"授权描述":"授权甲方给一个最终用户单位内部或者直接授权一个最终用户单位内部系统中使用此“产品”，并不授权其他任何第三方单位使用。产品本身不能单独对外销售。" },
        { "授权名称":"网站授权","授权ID":3,"授权描述":"授权甲方在一个网站上使用此“产品”，并不授权其他任何第三方网站使用。产品本身不能单独对外销售。" }
        ],
        "产品语言": [
        { "语言名称":"简体中文","语言ID":2 },
        { "语言名称":"繁体中文","语言ID":1 },
        { "语言名称":"简体中文","语言ID":2 },
        { "语言名称":"繁体中文","语言ID":2 },
        { "语言名称":"English","语言ID":2 },
        { "语言名称":"简体中文","语言ID":3 },
        { "语言名称":"繁体中文","语言ID":3 }
        ],
        "产品价格": [
        { "价格ID":1,"价格":5000},
        { "价格ID":1,"价格":8000},
        { "价格ID":1,"价格":10000},
        { "价格ID":1,"价格":20000},
        { "价格ID":2,"价格":50000},
        { "价格ID":2,"价格":80000},
        { "价格ID":2,"价格":90000},
        { "价格ID":2,"价格":10000},
        { "价格ID":2,"价格":1000},
        { "价格ID":3,"价格":5000},
        { "价格ID":3,"价格":6000},
        { "价格ID":3,"价格":7000},
        { "价格ID":3,"价格":8000},
        { "价格ID":3,"价格":10000 }
        ],
        "产品数量":[
        { "数量ID":1,"数量":1},
        { "数量ID":2,"数量":2},
        { "数量ID":3,"数量":3},
        { "数量ID":4,"数量":4},
        { "数量ID":5,"数量":5},
        { "数量ID":6,"数量":100},
        { "数量ID":7,"数量":200}
        ]
        
    }
};

//jsoneditor schema
const schema = {
    "title": "数据汇总",
    "description": "加载项相关数据",
    "type": "object",
    "properties": {
    "PassWord": {
        "title": "PassWord:",
        "description": "开启或关闭强制留痕的密码，不要随意更改，否则无法解除保护",
        "examples": ["112233"],
        "type": "string"
    },
    "JsonName": {
        "title": "JsonName:",
        "description": "GlobalData的文件名，不能修改",
        "type": "string"
    },
    "Version": {
        "title": "Version:",
        "description": "GlobalData.json的版本号，通过该版本号决定是否更新本地数据,不能修改",
    },
    "IsShowImportTestDataBtn": {
        "title": "IsShowImportTestDataBtn:",
        "description": "是否显示【导入测试数据】按钮",
        "type": "boolean"
    },
    "CFA": {
        "$ref": "CFA"
        },
    "SLC": {
        "$ref": "SLC"
        },
    "Contract": {
        "$ref": "Contract"
        },
    "Certfication": {
        "$ref": "Certfication"
        },
    "Product": {
        "$ref": "Product"
        }
    },
    "required": ["PassWord", "JsonName", "Version", "CFA", "Contract", "Certfication", "Product"]
};

//jsoneditor CFA
const CFA = {
    "title": "授权书生成工具相关设置",
    "type": "object",
    "required": ["SavePath","IsClearBeforeCreate","IsCreateNewContract","IsAddSign","TableData","DefaultData"],
    "properties": {
        "SavePath": {
            "title": "SavePath:",
            "description": "文档创建成功后保存的目录",
            "type": "string",
            "examples": [
            "windows: d:/doc/",
            "linux: /home/doc/"
            ]
        },
        "IsClearBeforeCreate": {
            "title": "IsClearBeforeCreate:",
            "description": "生成前是否清空文档",
            "type": "boolean"
        },
        "IsCreateNewContract": {
            "title": "IsCreateNewContract:",
            "description": "是否新建一个空白文档来生成",
            "type": "boolean"
        },
        "IsAddSign": {
            "title": "IsAddSign:",
            "description": "是否插入签名图片",
            "type": "boolean"
        },
        "TableData":{
            "title": "TableData:",
            "description": "存储授权书列表中的数据，如数据为空，则请通过界面中的【保存到本地】按钮即可产生",
        },
        "DefaultData":{
            "title": "DefaultData:",
            "description": "存储授权书新建表单中的默认数据，如数据为空，请通过界面中的【保存为默认值】按钮即可产生",
        }
    }
};

//jsoneditor SLC
const SLC = {
    "title": "服务承诺函生成工具相关设置",
    "type": "object",
    "required": ["SavePath","IsClearBeforeCreate","IsCreateNewContract","TableData","DefaultData"],
    "properties": {
        "SavePath": {
            "title": "SavePath:",
            "description": "文档创建成功后保存的目录",
            "type": "string",
            "examples": [
            "windows: d:/doc/",
            "linux: /home/doc/"
            ]
        },
        "IsClearBeforeCreate": {
            "title": "IsClearBeforeCreate:",
            "description": "生成前是否清空文档",
            "type": "boolean"
        },
        "IsCreateNewContract": {
            "title": "IsCreateNewContract:",
            "description": "是否新建一个空白文档来生成",
            "type": "boolean"
        },
        "IsAddSign": {
            "title": "IsAddSign:",
            "description": "是否插入签名图片",
            "type": "boolean"
        },
        "TableData":{
            "title": "TableData:",
            "description": "存储服务承诺函列表中的数据，如数据为空，则请通过界面中的【保存到本地】按钮即可产生",
        },
        "DefaultData":{
            "title": "DefaultData:",
            "description": "存储服务承诺函新建表单中的默认数据，如数据为空，请通过界面中的【保存为默认值】按钮即可产生",
        }
    }
}

//jsoneditor Contract
const Contract = {
    "title": "合同生成工具相关设置",
    "type": "object",
    "required": ["SavePath","IsClearBeforeCreate","IsCreateNewContract","IsTrackRevisions","LastDate","Count"],
    "properties": {
        "SavePath": {
            "title": "SavePath:",
            "description": "文档创建成功后保存的目录",
            "type": "string",
            "examples": [
            "windows: d:/doc/",
            "linux: /home/doc/"
            ]
        },
        "IsClearBeforeCreate": {
            "title": "IsClearBeforeCreate:",
            "description": "生成前是否清空文档",
            "type": "boolean"
        },
        "IsCreateNewContract": {
            "title": "IsCreateNewContract:",
            "description": "是否新建一个空白文档来生成",
            "type": "boolean"
        },
        "IsTrackRevisions": {
            "title": "IsTrackRevisions:",
            "description": "创建合同后，是否启动强制留痕",
            "type": "boolean"
        },
        "LastDate": {
            "title": "LastDate:",
            "description": "最后一次生成合同的日期，该次数与合同编号关联，如果该日期与当天不等则count重置",
            "type": "string"
        },
        "Count": {
            "title": "Count:",
            "description": "记录当天生成合同的次数，该次数于合同编号关联",
            "type": "number"
        },
    }
};

//jsoneditor Certfication
const Certfication = {
    "title": "资质生成工具相关设置",
    "type": "object",
    "required": ["SavePath","IsClearBeforeCreate","IsCreateNewContract","IsBold","FontSize","FontName","Left","Top","Width","Height","Data"],
    "properties": {
        "SavePath": {
            "title": "SavePath:",
            "description": "文档创建成功后保存的目录",
            "type": "string",
            "examples": [
            "windows: d:/doc/",
            "linux: /home/doc/"
            ]
        },
        "IsClearBeforeCreate": {
            "title": "IsClearBeforeCreate:",
            "description": "生成前是否清空文档",
            "type": "boolean"
        },
        "IsCreateNewContract": {
            "title": "IsCreateNewContract:",
            "description": "是否新建一个空白文档来生成",
            "type": "boolean"
        },
        "IsBold": {
            "title": "IsBold:",
            "description": "水印是否加粗",
            "type": "boolean"
        },
        "FontSize": {
            "title": "FontSize:",
            "description": "水印字体的大小",
            "type": "number"
        },
        "FontName": {
            "title": "FontName:",
            "description": "水印字体的名称",
            "type": "string"
        },
        "Left": {
            "title": "Left:",
            "description": "水印的横向距离",
            "type": "number"
        },
        "Top": {
            "title": "Top:",
            "description": "水印的纵向距离",
            "type": "number"
        },
        "Width": {
            "title": "Width:",
            "description": "水印的宽度",
            "type": "number"
        },
        "Height": {
            "title": "Height:",
            "description": "水印的高度",
            "type": "number"
        },
        "Data": {
            "title": "Data:",
            "description": "存储资质文件的信息",
            "type": "array"
        }
    }
}

//jsoneditor Product
const Product = {
    "title": "公司产品相关数据",
    "type": "object",
    "required": ["产品分类","产品版本","产品授权","产品语言","产品价格"],
    "properties": {
        "产品分类": {
            "title": "产品分类:",
            "description": "产品汇总，需要配置产品的名称、版本ID、授权ID、语言ID、价格ID、数量ID",
            "type": "array"
        },
        "产品版本": {
            "title": "产品版本:",
            "description": "版本汇总，需要配置版本的名称、版本ID、版本描述",
            "type": "array",
        },
        "产品授权": {
            "title": "产品授权:",
            "description": "授权汇总，需要配置授权的名称、授权ID、授权描述",
            "type": "array",
        },
        "产品语言": {
            "title": "产品语言:",
            "description": "语言汇总，需要配置语言的名称、语言描述",
            "type": "array",
        },
        "产品价格": {
            "title": "产品价格:",
            "description": "价格汇总，需要配置价格ID、价格",
            "type": "array",
        },
        "产品数量": {
            "title": "产品数量:",
            "description": "数量汇总，需要配置数量ID、数量",
            "type": "array",
        }
    }
}

//jsoneditor  插入的模板
const templates=[
    {
        text: '插入产品分类',
        title: '插入新的产品',
        field: '产品分类',
        value: {
        'ID': 1,
        '产品名称': '',
        '版本ID': 1,
        '授权ID': 1,
        '语言ID': 1,
        '价格ID': 1,
        '价格ID': 1,
        }
    },
    {
        text: '插入产品版本',
        title: '插入新的版本',
        field: '产品版本',
        value: {
        'ID': 1,
        '版本名称': '',
        '版本ID': 1,
        '版本描述': '',
        }
    },
    {
        text: '插入产品授权',
        title: '插入新的授权',
        field: '产品授权',
        value: {
        'ID': 1,
        '授权名称': '',
        '授权ID': 1,
        '授权描述': '',
        }
    },
    {
        text: '插入产品语言',
        title: '插入新的语言',
        field: '产品语言',
        value: {
        'ID': 1,
        '语言名称': '',
        '语言ID': 1,
        }
    },
    {
        text: '插入产品价格',
        title: '插入新的价格',
        field: '产品价格',
        value: {
        'ID': 1,
        '价格ID': 1,
        '价格': 5000,
        }
    },
    {
        text: '插入产品数量',
        title: '插入新的数量',
        field: '产品数量',
        value: {
        'ID': 1,
        '数量ID': 1,
        '数量': 1,
        }
    }
];