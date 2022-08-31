(function(factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD
        define(['jquery'], factory);
    } else if (typeof exports === 'object') {
        // CommonJS
        factory(require('jquery'));
    } else {
        // Browser globals
        factory(jQuery, window, document);
    }
}(function($, window, document, undefined) {


    /*************************策略对象*****************************/

    var RULES = {
        isNonEmpty: function(value, errorMsg) {
            //不能为空
            if (!value.length) {
                return errorMsg;
            }
        },
        isExit: function(value,chektype, errorMsg) {
            var flag;
            $.ajax({
                url:"/common/isExit",
                type : "post",
                dataType : "json",
                async: false,
                data:{"type":chektype,"testvalue":value},
                contentType : "application/x-www-form-urlencoded;charset=UTF-8",
                success:function(data){
                    if(data.isExit){
                        flag=1;

                    }else{
                        flag=0;
                    }
                    return flag;
                },
                error : function() {
                    alert("服务器发生故障！");
                }

            });
            if(flag==1){
                return errorMsg;
            }
        },
        minLength: function(value, length, errorMsg) {
            //大于
            if (value.length < length) {
                return errorMsg;
            }
        },
        maxLength: function(value, length, errorMsg) {
            //小于
            if (value.length < length) {
                return errorMsg;
            }
        },
        isMobile: function(value, errorMsg) {
            //是否为手机号码
            if (!/(^1[3|5|8][0-9]{9}$)/.test(value)) {
                return errorMsg;
            }
        },
        isEmail: function(value, errorMsg) {
            //是否为邮箱
            if (!/(^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$)/.test(value)) {
                return errorMsg;
            }
        },
        isPostcode: function(value, errorMsg) {
            //是否为邮箱
            if (!/[1-9]\d{5}(?!\d)/.test(value)) {
                return errorMsg;
            }
        },
        between: function(value, range, errorMsg) {
            //大于小于
            var min = parseInt(range.split('-')[0]);
            var max = parseInt(range.split('-')[1]);
            if (value < min || value> max) {
                return errorMsg;
            }
        },
        onlyEn: function(value, errorMsg) {
            //纯英文
            if (!/^[A-Za-z]+$/.test(value)) {

            }
        },
        onlyEnAndNum: function(value, errorMsg) {
            //英文数字
            if (!(/^[A-Za-z0-9]+$/.test(value))) {

            }
        },
        onlyZhAndEnAndNum: function(value, errorMsg) {
            //中文英文数字
            if (!/^[A-Za-z0-9\u4e00-\u9fa5]+$/.test(value)) {
                return errorMsg;
            }
        },
        onlyZh: function(value, errorMsg) {
            //纯中文
            if (!/^[\u4e00-\u9fa5]+$/.test(value)) {
                return errorMsg;
            }
        },
        onlyNum: function(value, errorMsg) {
            //数字包含小数
            if (!/^[0-9]+([.][0-9]+){0,1}$/.test(value)) {
                return errorMsg;
            }
        },
        onlyInt: function(value, errorMsg) {
            //整数
            if (!/^[0-9]*$/.test(value)) {
                return errorMsg;
            }
        },
        isChecked: function(value, errorMsg, el) {
            var i = 0;
            var $collection = $(el).find('input:checked');
            if(!$collection.length){
                return errorMsg;
            }
        },
        isCardid:function(idnumber, errorMsg) {

            var area={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"};

            var idcard,Y,JYM;
            var S,M;
            var idcard_array = new Array();
            idcard_array = idnumber.split("");
            //地区检验
            if(area[parseInt(idnumber.substr(0,2))]==null) {
                return  "地区非法!";
            }
            //身份号码位数及格式检验
            switch(idnumber.length){
                case 15:
                    if ( (parseInt(idnumber.substr(6,2))+1900) % 4 == 0 || ((parseInt(idnumber.substr(6,2))+1900) % 100 == 0 && (parseInt(idnumber.substr(6,2))+1900) % 4 == 0 )){
                        ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;//测试出生日期的合法性
                    } else {
                        ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;//测试出生日期的合法性
                    }
                    if(ereg.test(idnumber)){

                    }else{
                        return "出生日期超出范围或含有非法字符!";
                    }
                    break;
                case 18:
                    //18位身份号码检测
                    //出生日期的合法性检查
                    //闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
                    //平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
                    if ( parseInt(idnumber.substr(6,4)) % 4 == 0 || (parseInt(idnumber.substr(6,4)) % 100 == 0 && parseInt(idnumber.substr(6,4))%4 == 0 )){
                        ereg=/^[1-9][0-9]{5}[1-2](0|9)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//闰年出生日期的合法性正则表达式
                    } else {
                        ereg=/^[1-9][0-9]{5}[1-2](0|9)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//平年出生日期的合法性正则表达式
                    }
                    if(ereg.test(idnumber)){//测试出生日期的合法性
                        //计算校验位
                        S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
                            + (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9
                            + (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10
                            + (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5
                            + (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8
                            + (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4
                            + (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2
                            + parseInt(idcard_array[7]) * 1
                            + parseInt(idcard_array[8]) * 6
                            + parseInt(idcard_array[9]) * 3 ;
                        Y = S % 11;
                        M = "F";
                        JYM = "10X98765432";
                        M = JYM.substr(Y,1);//判断校验位
                        if(M == idcard_array[17]){
                        }else{
                            return "校验错误!";
                        }
                    }else{
                        return "出生日期超出范围或含有非法字符!";
                    }
                    break;
                default:
                    return "位数不对!";
                    break;
            }

        }
    };

    /*************************Validator类*****************************/

    var setting = {
        type: null,
        onBlur: null,
        onFocus: null,
        onChange: null,
        successTip: true
    };

    var Validator = function() {
        this.cache = [];
    };

    Validator.prototype.add = function(dom, rules,messageid) {
        var self = this;
        for (var i = 0, rule; rule = rules[i++];) {
            (function(rule) {
                var strategyAry = rule.strategy.split(':');
                var errorMsg = rule.errorMsg
                self.cache.push(function() {
                    var strategy = strategyAry.shift(); // 前删匹配方式并赋值
                    strategyAry.unshift(dom.value); // 前插value值
                    strategyAry.push(errorMsg); // 后插出错提示
                    strategyAry.push(dom); // 后插dom
                    if (!RULES[strategy]) {
                        $.error('没有' + strategy + '规则，请检查命名或自行定义');
                    }
                    return {
                        errorMsg: RULES[strategy].apply(dom, strategyAry),
                        el: dom
                    };
                });
            }(rule));
        }
    };

    Validator.prototype.start = function() {
        var result;
        for (var i = 0, validatorFunc; validatorFunc = this.cache[i++];) {
            var result = validatorFunc();
            if (setting.successTip) {
                new Validator().showMsg($(result.el), '', 1,$("#"+$(result.el).attr('data-show')));
            }
            if (result.errorMsg) {
                return result;
            }

        };
        return true;
    };

    Validator.prototype.showMsg = function(target, msg, status,messageid, callback) {
        //status
        // 0 : tip
        // 1 : success
        // 2 : error
        var _current = status ? (status > 1 ? 'error' : 'success') : 'tip';
        var $context = target.parent().parent();
        if(messageid!=null){
            var $context = messageid;
            $context.html("");
            $context.append(msg==""?"&nbsp;&nbsp;":msg);
            $context.css('visibility','visible');
        }else{
            var $msg = $context.find('.gr_red');
            $msg.remove();
            $context.append('<span class="gr_red">' + msg + '</span>');
        }
        // var _other = target.attr('data-type') || '';



    };

    var plugin = {
        init: function(options) {
            var $form = this;
            var $body = $('body');
            var $required = $form.find('.required');
            setting = $.extend(setting, options);

            if (setting.type) {
                $.extend(RULES, setting.type);
            }

            var validator = new Validator();

            $body.on({
                focus: function(event) {
                    var $this = $(this);
                    var _tipMsg = $this.attr('data-tip') || '';
                    var _status = $this.attr('data-status');
                    var _show=$this.attr('data-show');
                    if(_show==null){
                    }else{

                        var $messageid =  $("#"+_show);
                    }
                    if (_status == undefined ||!parseInt(_status)) {
                        validator.showMsg($this, _tipMsg,null,$messageid);
                    }
                    setting.onFocus ? setting.onFocus.call($this, arguments) : '';
                },
                blur: function(event) {
                    var $this = $(this);
                    var dataValid = $this.attr('data-valid');
                    var validLen = dataValid.split('||');
                    var errCollection = $this.attr('data-error');
                    var errMsgAry = errCollection.split("||");
                    var strategyAry, strategy, errMsg;
                    var _show=$this.attr('data-show');
                    if(_show==null){
                    }else{

                        var $messageid =  $("#"+_show);
                    }
                    for (var i = 0; i < validLen.length; i++) {
                        strategyAry = validLen[i].split(':');
                        strategy = strategyAry.shift();
                        strategyAry.unshift(this.value);
                        strategyAry.push(errMsgAry[i]);
                        strategyAry.push(this);
                        errMsg = RULES[strategy].apply(this, strategyAry);
                        if (errMsg) {
                            $this.attr('data-status', 0);
                            validator.showMsg($this,errMsg,2,$messageid);
                            break;
                        }
                    };

                    if (!errMsg) {
                        $this.attr('data-status', 1);
                        var successTip= setting.successTip;
                        if(successTip){
                            validator.showMsg($this, '', 1,$messageid);
                        }else{
                            validator.showMsg($this, '', 1,$messageid);
                        }
                    }

                    setting.onBlur ? setting.onBlur.call($this, arguments) : '';
                },
                change: function(event) {
                    setting.onChange ? setting.onChange.call($this, arguments) : '';
                }
            }, '.required');


        },
        submitValidate: function(options) {
            var $form = options || this;
            var $body = $('body');
            var $required = $form.find('.required');
            var validator = new Validator();
            var $target;

            $.each($required, function(index, el) {
                var $el = $(el);
                var dataValid = $el.attr('data-valid');
                var validLen = dataValid.split('||');
                var errCollection = $el.attr('data-error');
                var errMsgAry = errCollection.split("||");
                var _show=$el.attr('data-show');
                var ruleAry = [];

                for (var i = 0; i < validLen.length; i++) {
                    ruleAry.push({
                        strategy: validLen[i],
                        errorMsg: errMsgAry[i]
                    });
                };

                validator.add(el, ruleAry);

            });

            var result = validator.start();

            if (result.errorMsg) {
                $target = $(result.el);
                //$target.attr('data-status', 0)[0].focus();
                var id_show=$target.attr('data-show')
                validator.showMsg($target, result.errorMsg, 2,$("#"+id_show));
                return false;
            }


            return true;
        }
    };

    $.fn.validate = function() {
        var method = arguments[0];
        if (plugin[method]) {
            method = plugin[method];
            arguments = Array.prototype.slice.call(arguments, 1);
        } else if (typeof(method) == 'object' || !method) {
            method = plugin.init;
        } else {
            $.error('Method ' + method + ' does not exist on jQuery.validate Plugin');
            return this;
        }
        return method.apply(this, arguments);
    }

}))
