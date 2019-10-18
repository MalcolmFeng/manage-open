$(function () {
    initDayTotalCountEcharts();
    initTopApiCountEcharts();
    initTopApiInfo();
    initMonitorInfo();
});
var dayList = [];
var dayTotalCountList = [];
var dayTotalSuccessCountList = [];
var today;
var monitorData;
var dayTotalCountChart;

var topApiNameList = [];
var topApiNumList = [];
var topApiChart;

function initMonitorInfo() {
    $.ajax({
        url: context + "/service/dev/monitor/getMonitorInfo",
        type: "GET",
        success: function (data) {
            monitorData = data;
            $("#allTotalCount").text(data.allTotalCount);
            $("#allTotalSuccessCount").text(data.allTotalSuccessCount);
            $("#allTotalFalseCount").text(data.allTotalCount - data.allTotalSuccessCount);
            var allTotalSuccessPercent = data.allTotalCount == 0 ? 0 : data.allTotalSuccessCount / data.allTotalCount;
            $("#allTotalSuccessPercent").text((allTotalSuccessPercent * 100).toFixed(1) + "%");

            dayList = data.daylist;
            dayTotalCountList = data.dayTotalCountList;
            dayTotalSuccessCountList = data.dayTotalSuccessCountList;
            today = data.today;

            var todayTotalCount;
            var todayTotalSuccessCount;
            var todayTotalFalseCount;
            var todayTotalSuccessPercent;

            todayTotalCount = dayTotalCountList[dayTotalCountList.length - 1];
            todayTotalSuccessCount = dayTotalSuccessCountList[dayTotalSuccessCountList.length - 1];
            todayTotalFalseCount = todayTotalCount - todayTotalSuccessCount;
            todayTotalSuccessPercent = todayTotalCount == 0 ? 0 : todayTotalSuccessCount / todayTotalCount;

            $("#todayTotalCount").text(todayTotalCount);
            $("#todayTotalSuccessCount").text(todayTotalSuccessCount);
            $("#todayTotalFalseCount").text(todayTotalFalseCount);
            $("#todayTotalSuccessPercent").text((todayTotalSuccessPercent * 100).toFixed(1) + "%");

            updateDayTotalCountEcharts();
        }
    });
}

function initTopApiInfo() {
    $.ajax({
        url: context + "/service/dev/monitor/getTopApiInfo",
        type: "GET",
        success: function (data) {
            topApiNameList = data.topApiName;
            topApiNumList = data.topApiNum;
            updateTopApiCountEcharts()
        }
    });
}

function initDayTotalCountEcharts() {

    dayTotalCountChart = echarts.init(document.getElementById("dayTotalCountEcharts"));

    dayTotalCountChart.showLoading();

    dayTotalCountChart.setOption({
        backgroundColor: '#fff',
        title: {
            text: '每日调用量统计'
        },
        legend: {
            data: ['调用量', '成功量']
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#6a7985'
                }
            }
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        calculable: true,
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [{
            name: '调用量',
            type: 'line',
            symbol: 'none',
            smooth: 0.2,
            color: ['#66AEDE']
        },
            {
                name: '成功量',
                type: 'line',
                symbol: 'none',
                smooth: 0.2,
                color: ['#90EC7D']
            }]
    });


}

function updateDayTotalCountEcharts() {
    dayTotalCountChart.setOption({
        backgroundColor: '#fff',
        title: {
            text: '每日调用量统计'
        },
        xAxis: {
            data: dayList
        },
        yAxis: {},
        series: [{
            name: '成功量',
            data: dayTotalSuccessCountList
        }, {
            name: '调用量',
            data: dayTotalCountList
        }]
    });
    dayTotalCountChart.hideLoading();
}

function initTopApiCountEcharts() {
    topApiChart = echarts.init(document.getElementById('topApiCountEcharts'), null, {renderer: 'canvas'});

    topApiChart.showLoading();

    var option = {
        title: {
            text: '热门API TOP5七日榜',
            subtext: '最近7天成功调用最多的API前5名'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        legend: {
            data: ['调用量']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'value',
            boundaryGap: [0, 0.01]
        },
        yAxis: {
            type: 'category',
            data: ['', '', '', '', '']
        },
        series: [
            {
                color: '#2ec7c9',
                name: '调用量',
                type: 'bar',
                barWidth: 30,
                data: [0, 0, 0, 0, 0]
            }
        ]
    };

    topApiChart.setOption(option);
}

function updateTopApiCountEcharts() {
    topApiChart.setOption({
        backgroundColor: '#fff',
        title: {
            text: '热门API TOP5七日榜'
        },
        xAxis: {
            data: dayList
        },
        yAxis: {
            data: topApiNameList.length == 0 ? ['', '', '', '', ''] : topApiNameList
        },
        series: [{
            name: '调用量',
            data: topApiNumList.length == 0 ? [0, 0, 0, 0, 0] : topApiNumList
        }]
    });
    topApiChart.hideLoading();
}