let URL = "http://192.168.0.133:7777";

function getPeopleNearby(data) {
    console.log(data);
    let dataList = undefined;
    $.ajax({
        //请求方式
        type: 'GET',
        //发送请求的地址
        data: data,
        url: URL + "/love/jsGetResult",
        async : false,
        success: function (data) {
            dataList = data;
            console.log(data);
            // data.data.forEach(element => {
            //     initMapPoint(element.x,element.y)
            // });
        },
        error: function (jqXHR) {
            //请求失败函数内容
            console.log(jqXHR)
        }
    });
    return dataList.data;
};


function initMap(latitude, longitude,) {
    // locations.push({lat:latitude,lng:longitude});
    getPeopleNearby({latitude,longitude}).forEach(element => {
        locations.push({lat:element.y,lng:element.x})
    });;
    console.log(locations);
    //第一个参数传存放地图的标签，第二传参数是地图的配置为object格式
    const map = new google.maps.Map(document.getElementById("map"), {
        center: new google.maps.LatLng(latitude, longitude), //地图的中心点
        zoom: 10, //地图缩放比例
        mapTypeId: google.maps.MapTypeId.ROADMAP, //指定地图展示类型：卫星图像、普通道路
        mapTypeControl: false,//是否显示地图类型组件,false则不显示
        fullscreenControl: false, //是否显示全屏按钮,false则不显示
        scrollwheel: true, //是否允许滚轮滑动进行缩放
    });
    // 监听地图的点击事件
    map.addListener('click', (e) => {
        console.log(e);
        // map.panTo(e.LatLng);
        /* e.latLng 获取经纬度对象
         * e.latLng.lat(), e.latLng.lng()获取经度和纬度 */
        //可以进行增加marker，获取地址等操作
    });
    
    //遍历locations数组，添加到地图上
    const markers = locations.map((location, i) => {
        var marker = new google.maps.Marker({
            position: location,
            map: map,
        });
        //监听marker的点击事件
        marker.addListener('click', function () {
            // alert(i);
        });
    });
}