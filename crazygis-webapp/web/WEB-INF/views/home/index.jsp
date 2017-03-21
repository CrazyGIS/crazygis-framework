<%@ page import="com.crazygis.web.utils.WebUtils" %>
<%@ page import="com.crazygis.common.PropertiesManager" %>
<%@ page import="com.crazygis.web.page.PageResource" %><%--
  Created by IntelliJ IDEA.
  User: xuguolin
  Date: 2017/3/21
  Time: 14:15
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%
    //设置Title
    WebUtils.setTitle(request, "电子海图");
    PropertiesManager pm = PropertiesManager.getWebInstance();
%>
<ex-section id="headjs">
    <%=PageResource.scriptLink(
            "/map/api/ol/ol-debug.js",
            "/scripts/UI/ui.tools.js",
            "/scripts/UI/ui.tabPanel.js",
            "/scripts/UI/ui.optionList.js",
            "/scripts/UI/ui.form.js",
            "/scripts/UI/ui.color.js",
            "/scripts/UI/ui.selectionList.js"
    )%>
</ex-section>

<ex-section id="excss">
    <%=PageResource.cssLink(
            "/map/api/ol/ol-debug.css",
            "/content/themes/metro/ui.metro.tabPanel.css",
            "/content/themes/metro/ui.metro.optionList.css",
            "/content/themes/metro/ui.metro.form.css",
            "/content/themes/metro/ui.metro.images.css",
            "/content/themes/metro/ui.metro.tools.css",
            "/content/themes/metro/ui.metro.selectionList.css"
    )%>
    <style type="text/css">
        #mapContainer {
            width: 100%;
            height: 100%;
        }

        div.map-tool-background {
            position: absolute;
            width: 100%;
            height: 100%;
            background-color: #F1F1F1;
            opacity: .9;
            filter: Alpha(opacity=90);
        }

        div.map-tool-content {
            position: absolute;
            width: 100%;
            height: 100%;
        }
    </style>
</ex-section>

<body>
<div id="contentPanel" class="content-panel" style="background-color:#F1F1F1;position:relative;">
    <div id="toolbar" class="toolbar" style="position:absolute;z-index:1000;background-color:transparent;border-bottom:1px solid #DCDCDC;">
        <div class="map-tool-background"></div>
        <div class="map-tool-content">
            <ul class="tools">
                <li>
                    <button id="zoomIn" class="icon-button" title="放大">
                        <i class="fa fa-plus"></i>
                    </button>
                </li>
                <li>
                    <button id="zoomOut" class="icon-button" title="缩小">
                        <i class="fa fa-minus"></i>
                    </button>
                </li>
                <li>
                    <button id="home" class="icon-button" title="默认范围">
                        <i class="fa fa-home"></i>
                    </button>
                </li>
                <li>
                    <button id="layersControl" class="icon-button" title="图层">
                        <i class="fa fa-list"></i>
                    </button>
                </li>
            </ul>
        </div>
    </div>
    <div id="mapContainer"></div>
</div>
</body>

<ex-section id="exjs">
    <script type="text/javascript">
        (function() {
            var pageLogic = window.pageLogic = {
                init: {
                    //地图资源
                    map: function() {
                        var projection, map;
                        var aisTileLayer;
                        var aisVectorSource;
                        var aisFeatureLayer;

                        projection = pageLogic.projection = ol.proj.get('EPSG:4326');

                        map = pageLogic.map = new ol.Map({
                            controls: ol.control.defaults({
                                attribution: false,
                                rotate: false,
                                zoom: false
                            }).extend([
                                new ol.control.ScaleLine()
                            ]),
                            layers: [
                                new ol.layer.Tile({
                                    id: "network_vector_base",
                                    name: "矢量图层",
                                    source: new ol.source.XYZ({
                                        projection: projection,
                                        url: "http://t{0-7}.tianditu.com/DataServer?T=vec_c&x={x}&y={y}&l={z}"
                                    })
                                }),
                                new ol.layer.Tile({
                                    id: "network_vector_note",
                                    name: "矢量注记图层",
                                    source: new ol.source.XYZ({
                                        projection: projection,
                                        url: "http://t{0-7}.tianditu.com/DataServer?T=cva_c&x={x}&y={y}&l={z}"
                                    })
                                })
                            ],
                            view: new ol.View({
                                projection: projection,
                                center: [118.78, 32.0865],
                                zoom: 10,
                                minZoom: 5,
                                maxZoom: 18
                            }),
                            target: 'mapContainer'
                        });

//                        // 添加AIS瓦片图层
//                        aisTileLayer = pageLogic.aisTileLayer = new ol.layer.Tile({
//                            id: 'ais',
//                            name: "AIS船舶",
//                            source: new ol.source.TileWMS({
//                                url: 'http://localhost:8888/geoserver/crazygis/wms',
//                                params: {
//                                    'FORMAT': 'image/png',
//                                    'VERSION': '1.1.1',
//                                    tiled: true,
//                                    STYLES: '',
//                                    LAYERS: 'crazygis:dynamic_ship',
//                                    tilesOrigin: 118.396537780762 + "," + 31.848030090332
//                                }
//                            }),
//                            minResolution: 0.000171661376953125, // 13
//                            maxResolution: 1.40625              // 0
//                        });
//
//                        map.addLayer(aisTileLayer);
//
//                        // AIS数据源
//                        aisVectorSource = pageLogic.aisVectorSource = new ol.source.Vector({
//                            format: new ol.format.GeoJSON(),
//                            url: function (extent) {
//                                return 'http://localhost:8888/geoserver/crazygis/wfs?service=WFS&' +
//                                    'version=1.1.0&request=GetFeature&typename=crazygis:dynamic_ship&' +
//                                    'outputFormat=application/json&srsname=EPSG:4326&' +
//                                    'bbox=' + extent.join(',') + ',EPSG:4326' + '&t=' + new Date().getTime();
//                            },
//                            strategy: ol.loadingstrategy.bbox
//                        });
//                        // 添加AIS要素图层
//                        aisFeatureLayer = pageLogic.aisFeatureLayer = new ol.layer.Vector({
//                            source: aisVectorSource,
//                            style: pageLogic.shipRender.getShipStyle,
//                            minResolution: 0.000005364418029785156, // 18
//                            maxResolution: 0.000171661376953125  // 13
//                        });
//
//                        map.addLayer(aisFeatureLayer);
                    },
                    //在布局计算前创建控件
                    before: function () {

                    },
                    //布局计算
                    layout: function () {
                        masterpage.resize(function () {
                            //40 是toolbar的高度
                            var height = masterpage.contentBodyHeight - 40,
                                width = masterpage.contentBodyWidth;
                            $("#contentPanel").css("height", height + 40 + "px");
                        });
                        if(pageLogic.map) {
                            pageLogic.map.updateSize();
                        }
                    },
                    //在布局计算后创建控件
                    after: function () {
                        
                    },
                    //页面控件的事件绑定，一般是页面按钮的事件绑定
                    events: function () {
                        var that = this;
                        $("#zoomIn").click(function (e) {

                        });

                        $("#zoomOut").click(function (e) {

                        });

                        $("#home").click(function (e) {

                        });

                        
                    },
                    //初始化
                    load: function() {
                        // 更新动态船舶信息
                        //setInterval(pageLogic.shipRender.updateDynamicShip, 10000);
                    }
                },
                shipRender: {
                    updateDynamicShip: function () {
                        pageLogic.aisTileLayer.getSource().updateParams({'TIME': new Date().toISOString()});
                        pageLogic.aisVectorSource.refresh();
                        pageLogic.aisVectorSource.clear();
                    },
                    getShipStyle: function (feature, resolution) {
                        // 船舶数据
                        var shipData = feature.getProperties();
                        // 坐标
                        var coordinates = shipData.geometry.getCoordinates();

                        // 图标
                        var lengthPixel = pageLogic.shipRender.meter2Pixel(shipData.length, resolution, coordinates[1]);
                        var widthPixel = pageLogic.shipRender.meter2Pixel(shipData.width, resolution, coordinates[1]);
                        var color = "#FFFF00";
                        var rotation = 0;
                        if(shipData.heading) {
                            rotation = shipData.heading * Math.PI / 180;
                        } else if(shipData.direction) {
                            rotation = shipData.direction * Math.PI / 180;
                        }

                        var image = pageLogic.shipRender.getShipIcon(lengthPixel, widthPixel, shipData.speed, shipData.rot, color, rotation);

                        // 标注
                        var text = null;
                        if (resolution < 0.0000858306884765625) {
                            text = pageLogic.shipRender.getShipTextStyle(shipData.mmsi, widthPixel, rotation);
                        }

                        var style = new ol.style.Style({
                            image: image,
                            text: text
                        });

                        return style;
                    },
                    meter2Pixel: function (meter, resolution, lat) {
                        // 米转为度
                        var scaleParam = (Math.cos(lat * Math.PI / 180) + 1) / 2;  // 尺度参数
                        var earthPerimeter = 6378137 * 2 * Math.PI;  // 地球周长(米)
                        var degree = 360 * meter / earthPerimeter * scaleParam;   // 米转为度的结果

                        // 度转为像素
                        var pixel = Math.round(degree / resolution);

                        return pixel;
                    },
                    getShipTextStyle: function (shipName, widthPixel, rotation) {
                        if(widthPixel < 8) {
                            widthPixel = 14;
                        }
                        // 偏移量计算
                        var offset = Math.round(widthPixel / 2) + 10;
                        var deltaRadian = 0;
                        var xDirection = 1;  // x的偏移方向
                        if(rotation >= 0 && rotation < Math.PI / 2) {
                            deltaRadian = Math.PI / 2 - rotation;
                            xDirection = 1;
                        } else if(rotation >= Math.PI / 2 && rotation < Math.PI) {
                            deltaRadian = rotation - Math.PI / 2;
                            xDirection = -1;
                        } else if(rotation >= Math.PI && rotation < 3 * Math.PI / 2) {
                            deltaRadian = 3 * Math.PI / 2 - rotation;
                            xDirection = 1;
                        } else {
                            deltaRadian = rotation - 3 * Math.PI / 2;
                            xDirection = -1;
                        }

                        var offsetX = offset * Math.sin(deltaRadian) * xDirection;
                        var offsetY = offset * Math.cos(deltaRadian);
                        // 旋转角度计算
                        if(rotation > Math.PI && rotation < 2 * Math.PI) {
                            rotation = rotation + Math.PI / 2;
                        } else {
                            rotation = rotation - Math.PI / 2;
                        }

                        var text = new ol.style.Text({
                            text: shipName,
                            font: "12px Microsoft YaHei,sans-serif",
                            offsetX: offsetX,
                            offsetY: offsetY,
                            textAlign: 'center',
                            stroke: new ol.style.Stroke({
                                color: [245, 244, 238, 0.8],
                                width: 3
                            }),
                            fill: new ol.style.Fill({
                                color: [0, 0, 0, 1]
                            }),
                            rotation: rotation
                        });

                        return text;
                    },
                    getShipIcon: function (lengthPixel, widthPixel, speed, rot, color, rotation) {
                        var speedPixel = 20;  // 速度符号的长度
                        // 船舶的长宽都处理为偶数
                        lengthPixel = Math.round(lengthPixel / 2) * 2;
                        widthPixel = Math.round(widthPixel / 2) * 2;
                        // 船舶的长宽比如果不足4:1, 强行修正到4:1, 这样在绘制的时候，更有利于绘制出船的形状。
                        if(lengthPixel / widthPixel < 4) {
                            lengthPixel = widthPixel * 4;
                        }
                        // 船舶状态
                        var shipStatus = "static";
                        // 如果船速大于等于0.5节
                        if (speed && speed >= 0.5) {
                            // 如果转向率不等于0
                            if (rot && rot != 0) {
                                // 转向率大于0，右转
                                if (rot > 0) {
                                    shipStatus = "right";
                                } else {
                                    shipStatus = "left";
                                }
                            } else {
                                shipStatus = "front";
                            }

                            speedPixel = 20 + Math.floor(speed / 5) * 5;
                        } else {
                            shipStatus = "static";
                        }

                        var anchor;
                        var width, height, points, fill, stroke;

                        fill = color || "#FFFF00";
                        stroke = "#000000";

                        // 如果宽度的像素大于等于8px,则根据实际大小绘制船舶形状。此时，速度为speedPixel, 转向为5px
                        if(widthPixel >= 8) {
                            var i = 0, ship;
                            var x = lengthPixel, y = widthPixel;
                            var shipBodyPoints = [];
                            // 船体点包含(p2->p11,其中p2=p11)
                            shipBodyPoints.push([(y + 2) / 2, 1]);  // p2
                            shipBodyPoints.push([0.25 * y + 1, 0.5 * y + 1]); // p3
                            shipBodyPoints.push([1, 1.5 * y + 1]);  // p4
                            shipBodyPoints.push([1, x - 0.75 * y + 1]);  // p5
                            shipBodyPoints.push([y / 6 + 1, x + 1]);  // p6
                            shipBodyPoints.push([5 * y / 6 + 1, x + 1]);  // p7
                            shipBodyPoints.push([y + 1, x - 0.75 * y + 1]);  // p8
                            shipBodyPoints.push([y + 1, 1.5 * y + 1]);  // p9
                            shipBodyPoints.push([0.75 * y + 1, 0.5 * y + 1]);  // p10
                            shipBodyPoints.push([(y + 2) / 2, 1]);  // p11=p2

                            switch (shipStatus) {
                                case "static":
                                    height = x + 2;
                                    width = y + 2;
                                    anchor = [(y + 2) / 2, (x + 2) / 2];
                                    // 点位已足够，不用再添加
                                    // 点位转字符串
                                    points = pageLogic.shipRender.points2String(shipBodyPoints);
                                    break;
                                case "front":
                                    height = x + 2 + speedPixel;
                                    width = y + 2;
                                    anchor = [(y + 2) / 2, (x + 2) / 2 + speedPixel];
                                    // 添加速度符号点位
                                    // 所有点位Y值+speedPixel
                                    for(i = 0; i < shipBodyPoints.length; i++) {
                                        ship = shipBodyPoints[i];
                                        ship[1] += speedPixel;
                                    }
                                    // 往数组头部添加点
                                    shipBodyPoints.unshift([(y + 2) / 2, 1]);  // p1
                                    // 往数组尾部添加点
                                    shipBodyPoints.push([(y + 2) / 2, 1]);  // p12
                                    // 点位转字符串
                                    points = pageLogic.shipRender.points2String(shipBodyPoints);
                                    break;
                                case "left":
                                    height = x + 2 + speedPixel;
                                    width = y + 2;
                                    anchor = [(y + 2) / 2, (x + 2) / 2 + speedPixel];
                                    // 添加速度和左转弯符号点位
                                    // 所有点位Y值+speedPixel
                                    for(i = 0; i < shipBodyPoints.length; i++) {
                                        ship = shipBodyPoints[i];
                                        ship[1] += speedPixel;
                                    }
                                    // 往数组头部添加点
                                    shipBodyPoints.unshift([(y + 2) / 2, 1]);  // p1
                                    shipBodyPoints.unshift([(y + 2) / 2 - 5, 1]);  // p0
                                    // 往数组尾部添加点
                                    shipBodyPoints.push([(y + 2) / 2, 1]);  // p12
                                    shipBodyPoints.push([(y + 2) / 2 - 5, 1]);  // p13
                                    // 点位转字符串
                                    points = pageLogic.shipRender.points2String(shipBodyPoints);
                                    break;
                                case "right":
                                    height = x + 2 + speedPixel;
                                    width = y + 2;
                                    anchor = [(y + 2) / 2, (x + 2) / 2 + speedPixel];
                                    // 添加速度和右转弯符号点位
                                    // 所有点位Y值+speedPixel
                                    for(i = 0; i < shipBodyPoints.length; i++) {
                                        ship = shipBodyPoints[i];
                                        ship[1] += speedPixel;
                                    }
                                    // 往数组头部添加点
                                    shipBodyPoints.unshift([(y + 2) / 2, 1]);  // p1
                                    shipBodyPoints.unshift([(y + 2) / 2 + 5, 1]);  // p0
                                    // 往数组尾部添加点
                                    shipBodyPoints.push([(y + 2) / 2, 1]);  // p12
                                    shipBodyPoints.push([(y + 2) / 2 + 5, 1]);  // p13
                                    // 点位转字符串
                                    points = pageLogic.shipRender.points2String(shipBodyPoints);
                                    break;
                                default:
                                    break;
                            }
                        } else { // 如果宽度的像素小于8px,则绘制为三角形的船舶，按照已有方案执行
                            switch (shipStatus) {
                                case "static":
                                    width = 16;
                                    height = 20;
                                    anchor = [8, 10];
                                    points = "8,1 14,19 2,19";
                                    break;
                                case "front":
                                    width = 16;
                                    height = 30;
                                    anchor = [8, 11];
                                    points = "8,1 8,11 14,29 2,29 8,11";
                                    break;
                                case "left":
                                    width = 16;
                                    height = 30;
                                    anchor = [8, 11];
                                    points = "4,1 8,1 8,11 14,29 2,29 8,11, 8,1";
                                    break;
                                case "right":
                                    width = 16;
                                    height = 30;
                                    anchor = [8, 11];
                                    points = "12,1 8,1 8,11 14,29 2,29 8,11, 8,1";
                                    break;
                                default:
                                    break;
                            }
                        }

                        var svgContent = '<svg width="'+ width +'" height="'+height+'" version="1.1" xmlns="http://www.w3.org/2000/svg">'
                            + '<polygon points="'+ points +'" style="fill:'+ fill +';stroke:'+ stroke +';stroke-width:1" />'
                            + '</svg>';

                        var icon = new ol.style.Icon({
                            anchorXUnits: 'pixels',
                            anchorYUnits: 'pixels',
                            anchor: anchor,
                            src: 'data:image/svg+xml;utf8,' + svgContent,
                            rotation: rotation
                        });

                        return icon;
                    },
                    points2String: function (points) {
                        var result = [];
                        var point;
                        for(var i = 0; i < points.length; i++) {
                            point = points[i];
                            result.push(point.join(","));
                        }

                        return result.join(" ");
                    }
                }
            };
        })();
    </script>
</ex-section>
