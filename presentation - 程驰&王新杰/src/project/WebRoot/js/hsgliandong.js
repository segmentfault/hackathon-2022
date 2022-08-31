
var city=new Array();
var county=new Array();
var bj=new Array();

city[0]= new Array("10","叶");
city[1]= new Array("20","果实");
city[2]= new Array("30","根茎");
city[3]= new Array("40","树皮");



county[0]= new Array("1001","小叶");
county[1]= new Array("1002","叶斑生小粒");

county[2]= new Array("2001","干缩V腐烂");

county[3]= new Array("2002","脱落V早落");

county[4]= new Array("3001","根茎");

county[5]= new Array("4001","粗燥V开裂");


bj[0]= new Array("100101","叶片变黄或黄百色");
bj[1]= new Array("100102","顶端叶片焦黄");
bj[2]= new Array("100201","黄色小孢");
bj[3]= new Array("100202","黄褐色角斑");
bj[4]= new Array("200101","锈斑");
bj[5]= new Array("200102","淡黄色水渍状斑块");
bj[6]= new Array("200103","浅褐色条纹");
bj[7]= new Array("200104","湿润状小斑点");
bj[8]= new Array("200105","“T”字形开裂");

bj[9]= new Array("200201","黄褐色斑块");
bj[10]= new Array("200202","圆形凹陷病斑");
bj[11]= new Array("200203","淡黄色水渍状斑点");

bj[12]= new Array("300101","水渍状褐色病斑");
bj[13]= new Array("300102","癌瘤（球形或扁球形）");
bj[14]= new Array("300103","病瘤青灰色或肉红色");
bj[15]= new Array("300104","皮色发暗，无光泽，皮层发褐色或黑褐色");

bj[16]= new Array("400101","树皮易脱落");
function innit_area(f_obj){
	//生成一级目录
	f_obj.length=0;
	f_obj.options[f_obj.length]=new Option("请选择...","0");
	for(i=0;i<city.length;i++){
		f_obj.options[f_obj.length]=new Option(city[i][1],city[i][0]);
	}
}
function change_area(f_obj,s_obj){
	//生成二级目录
	s_obj.length=0;
	s_obj.options[s_obj.length]=new Option("请选择...","0");
	if (f_obj.value!=0  ){
		for(i=0;i<county.length;i++){
			if (county[i][0].substring(0,2)==f_obj.value){
			s_obj.options[s_obj.length]=new Option(county[i][1],county[i][0]);
			}
		}
	}
}

function change_area3(f_obj,s_obj){
	//生成二级目录
	s_obj.length=0;
	s_obj.options[s_obj.length]=new Option("请选择...","0");
	if (f_obj.value!=0  ){
		for(i=0;i<bj.length;i++){
			if (bj[i][0].substring(0,4)==f_obj.value){
			s_obj.options[s_obj.length]=new Option(bj[i][1],bj[i][0]);
			}
		}
	}
}
