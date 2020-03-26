layui.use('laytpl', function(){

//时间戳的处理
layui.laytpl.toDateString = function(d, format){
  var date = new Date(d || new Date())
  ,ymd = [
    this.digit(date.getFullYear(), 4)
    ,this.digit(date.getMonth() + 1)
    ,this.digit(date.getDate())
  ]
  ,hms = [
    this.digit(date.getHours())
    ,this.digit(date.getMinutes())
    ,this.digit(date.getSeconds())
  ];

  format = format || 'yyyy-MM-dd HH:mm:ss';

  return format.replace(/yyyy/g, ymd[0])
  .replace(/MM/g, ymd[1])
  .replace(/dd/g, ymd[2])
  .replace(/HH/g, hms[0])
  .replace(/mm/g, hms[1])
  .replace(/ss/g, hms[2]);
};

//时间戳的处理
layui.laytpl.toDateStringNull = function(d, format){
	if(null == d) {
		return "";
	}
var date = new Date(d)
,ymd = [
this.digit(date.getFullYear(), 4)
,this.digit(date.getMonth() + 1)
,this.digit(date.getDate())
]
,hms = [
this.digit(date.getHours())
,this.digit(date.getMinutes())
,this.digit(date.getSeconds())
];

format = format || 'yyyy-MM-dd HH:mm:ss';

return format.replace(/yyyy/g, ymd[0])
.replace(/MM/g, ymd[1])
.replace(/dd/g, ymd[2])
.replace(/HH/g, hms[0])
.replace(/mm/g, hms[1])
.replace(/ss/g, hms[2]);
};
 
//数字前置补零
layui.laytpl.digit = function(num, length, end){
  var str = '';
  num = String(num);
  length = length || 2;
  for(var i = num.length; i < length; i++){
    str += '0';
  }
  return num < Math.pow(10, length) ? str + (num|0) : num;
};

})	


	var CXL = {
    };
    //系统共用的一些弹出框
	CXL.info = function (info) {
	    top.layer.msg(info, {icon: 6});
	};
	CXL.success = function (info) {
	    top.layer.msg(info, {icon: 1});
	};
	CXL.error = function (info) {
	    top.layer.msg(info, {icon: 2});
	};
	CXL.question = function (info) {
	    top.layer.msg(info, {icon: 3});
	};
	CXL.lock = function (info) {
	    top.layer.msg(info, {icon: 4});
	};
	CXL.warn = function (info) {
	    top.layer.msg(info, {icon: 5});
	};
	CXL.confirm = function (tip, ensure) {
	    top.layer.confirm(tip, {
	        skin: 'layui-layer-admin'
	    }, function () {
	        ensure();
	    });
	};
	CXL.getTextByJs = function(arr) {
		 var str = "";
	       for (var i = 0; i < arr.length; i++) {
	           str += arr[i].id+ ",";
	      }
	       //去掉最后一个逗号(如果不需要去掉，就不用写)
	      if (str.length > 0) {
	          str = str.substr(0, str.length - 1);
	      }
	      return str;
	}