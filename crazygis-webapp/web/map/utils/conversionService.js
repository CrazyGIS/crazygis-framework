/**
 * Created by xuguolin on 2015/6/7.
 */

var conversionService = window.conversionService = {
    // 经度：Lng,纬度：Lat
    // WGS-84：是国际标准，GPS坐标（Google Earth使用、或者GPS模块）
    // GCJ-02：中国坐标偏移标准，Google Map、高德、腾讯使用
    // BD-09：百度坐标偏移标准，Baidu Map使用

    PI: Math.PI,
    x_pi: Math.PI * 3000.0 / 180.0,
    delta: function (lng, lat) {
        // Krasovsky 1940
        //
        // a = 6378245.0, 1/f = 298.3
        // b = a * (1 - f)
        // ee = (a^2 - b^2) / a^2;
        var a = 6378245.0;
        var ee = 0.00669342162296594323;
        var dLat = this.transformLat(lng - 105.0, lat - 35.0);
        var dLng = this.transformLng(lng - 105.0, lat - 35.0);
        var radLat = lat / 180.0 * this.PI;
        var magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        var sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * this.PI);
        dLng = (dLng * 180.0) / (a / sqrtMagic * Math.cos(radLat) * this.PI);
        return { 'lat': dLat, 'lng': dLng };
    },
    //WGS-84 to GCJ-02
    wgs84_To_gcj02: function (wgsLng, wgsLat) {
        if (this.outOfChina(wgsLng, wgsLat))
            return { 'lat': wgsLat, 'lng': wgsLng };

        var d = this.delta(wgsLng, wgsLat);
        var gcjLat = wgsLat + d.lat;
        var gcjLng = wgsLng + d.lng;
        return { 'lat': gcjLat, 'lng': gcjLng };
    },
    //GCJ-02 to WGS-84
    gcj02_To_wgs84: function (gcjLng, gcjLat) {
        if (this.outOfChina(gcjLng, gcjLat))
            return { 'lat': gcjLat, 'lng': gcjLng };

        var d = this.delta(gcjLng, gcjLat);
        var wgsLat = gcjLat - d.lat;
        var wgsLng = gcjLng - d.lng;
        return { 'lat': wgsLat, 'lng': wgsLng };
    },
    //GCJ-02 to WGS-84 exactly
    gcj02_To_wgs84_exact: function (gcjLng, gcjLat) {
        var initDelta = 0.01;
        var threshold = 0.000000001;
        var dLat = initDelta, dLng = initDelta;
        var mLat = gcjLat - dLat, mLng = gcjLng - dLng;
        var pLat = gcjLat + dLat, pLng = gcjLng + dLng;
        var wgsLat, wgsLng, i = 0;
        while (1) {
            wgsLat = (mLat + pLat) / 2;
            wgsLng = (mLng + pLng) / 2;
            var tmp = this.wgs84_To_gcj02(wgsLng, wgsLat)
            dLat = tmp.lat - gcjLat;
            dLng = tmp.lng - gcjLng;
            if ((Math.abs(dLat) < threshold) && (Math.abs(dLng) < threshold))
                break;

            if (dLat > 0) pLat = wgsLat; else mLat = wgsLat;
            if (dLng > 0) pLng = wgsLng; else mLng = wgsLng;

            if (++i > 10000) break;
        }
        return { 'lat': wgsLat, 'lng': wgsLng };
    },
    //GCJ-02 to BD-09
    gcj02_To_bd09: function (gcjLng, gcjLat) {
        var x = gcjLng, y = gcjLat;
        var z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * this.x_pi);
        var theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * this.x_pi);
        bdLng = z * Math.cos(theta) + 0.0065;
        bdLat = z * Math.sin(theta) + 0.006;
        return { 'lat': bdLat, 'lng': bdLng };
    },
    //BD-09 to GCJ-02
    bd09_To_gcj02: function (bdLng, bdLat) {
        var x = bdLng - 0.0065, y = bdLat - 0.006;
        var z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * this.x_pi);
        var theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * this.x_pi);
        var gcjLng = z * Math.cos(theta);
        var gcjLat = z * Math.sin(theta);
        return { 'lat': gcjLat, 'lng': gcjLng };
    },
    // wgs-84转mercator
    wgs84_To_mercator: function (lng, lat) {
        var merX = lng * 20037508.342787 / 180;
        var merY = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
        merY = merY * 20037508.342787 / 180;
        return {
            x: merX,
            y: merY
        }
    },
    // mercator转wgs-84
    mercator_To_wgs84: function (x, y) {
        var lng = x / 20037508.342787 * 180;
        var lat = y / 20037508.342787 * 180;
        lat = 180 / Math.PI
				* (2 * Math.atan(Math.exp(lat * Math.PI / 180)) - Math.PI / 2);
        return {
            x: lng,
            y: lat
        }
    },
    distance: function (lngA, latA, lngB, latB) {
        var earthR = 6371000;
        var x = Math.cos(latA * Math.PI / 180) * Math.cos(latB * Math.PI / 180) * Math.cos((lngA - lngB) * Math.PI / 180);
        var y = Math.sin(latA * Math.PI / 180) * Math.sin(latB * Math.PI / 180);
        var s = x + y;
        if (s > 1)
            s = 1;
        if (s < -1)
            s = -1;
        var alpha = Math.acos(s);
        var distance = alpha * earthR;
        return distance;
    },
    outOfChina: function (lng, lat) {
        if (lng < 72.004 || lng > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    },
    transformLat: function (x, y) {
        var ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * this.PI) + 20.0 * Math.sin(2.0 * x * this.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * this.PI) + 40.0 * Math.sin(y / 3.0 * this.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * this.PI) + 320 * Math.sin(y * this.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    },
    transformLng: function (x, y) {
        var ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * this.PI) + 20.0 * Math.sin(2.0 * x * this.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * this.PI) + 40.0 * Math.sin(x / 3.0 * this.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * this.PI) + 300.0 * Math.sin(x / 30.0 * this.PI)) * 2.0 / 3.0;
        return ret;
    }
};
