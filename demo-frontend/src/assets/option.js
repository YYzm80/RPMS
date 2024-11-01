import {ref} from "vue";

let data = ref({
    studentNum: 0,
    resumeNum: 0,
    companyNum: 0,
    recruitmentNum: 0,
    interviewNum: 0,
    interviewRate: 0.0,
    interviewPassRate: 0.0
})

function getChartData(orgData) {
    data.value = orgData
    if (data.value.length !== 0) {
        let Max = Math.max(data.value.studentNum,
            data.value.resumeNum, data.value.companyNum,
            data.value.recruitmentNum, data.value.interviewNum) + 5
        const option1 = init(Max).option1;
        const option2 = init(Max).option2;
        const option3 = init(Max).option3;
        return {
            option1,
            option2,
            option3
        }
    }
}

function init(Max) {
    const option1 = {
        title: {
            text: '参会情况'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            left: 'center',
            data: [
                '参会情况',
            ]
        },
        radar: [
            {
                indicator: [
                    {text: '总参会人数', max: Max},
                    {text: '总投递数量', max: Max},
                    {text: '参会企业数', max: Max},
                    {text: '招聘岗位数', max: Max},
                    {text: '面试场次数', max: Max},
                ],
                radius: 100
            },
        ],
        series: [
            {
                type: 'radar',
                tooltip: {
                    trigger: 'item'
                },
                areaStyle: {},
                data: [
                    {
                        value: [data.value.studentNum, data.value.resumeNum, data.value.companyNum, data.value.recruitmentNum, data.value.interviewNum],
                        name: '参会情况'
                    }
                ]
            }
        ]
    }

    const option2 = {
        title: {
            text: '投递/面试率'
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            top: '5%',
            left: 'center'
        },
        series: [
            {
                name: '投递/面试率%',
                type: 'pie',
                radius: ['40%', '70%'],
                avoidLabelOverlap: false,
                itemStyle: {
                    borderRadius: 10,
                    borderColor: '#fff',
                    borderWidth: 2
                },
                label: {
                    show: false,
                    position: 'center'
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: 20,
                        fontWeight: 'bold'
                    }
                },
                labelLine: {
                    show: false
                },
                data: [
                    {value: data.value.interviewRate, name: '接到面试', itemStyle: {color: '#5ab726'}},
                    {value: 100 - data.value.interviewRate, name: '简历未通过', itemStyle: {color: '#ff375f'}}
                ]
            }
        ]
    }

    const option3 = {
        title: {
            text: '面试通过率'
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            top: '5%',
            left: 'center'
        },
        series: [
            {
                name: '面试通过率%',
                type: 'pie',
                radius: ['40%', '70%'],
                avoidLabelOverlap: false,
                itemStyle: {
                    borderRadius: 10,
                    borderColor: '#fff',
                    borderWidth: 2
                },
                label: {
                    show: false,
                    position: 'center'
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: 20,
                        fontWeight: 'bold'
                    }
                },
                labelLine: {
                    show: false
                },
                data: [
                    {value: data.value.interviewPassRate, name: '通过', itemStyle: {color: '#5ab726'}},
                    {value: 100 - data.value.interviewPassRate, name: '未通过', itemStyle: {color: '#ff375f'}}
                ]
            }
        ]
    }
    return {
        option1,
        option2,
        option3
    }
}

export {getChartData}