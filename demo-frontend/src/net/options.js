import {ref} from "vue";

let data = ref({
    month: '',
    basicCommunityStat: {
        livingCount: 0,
        emptyPropertyRate: 0.0,
        complaintCount: 0,
        repairCount: 0
    },
    managerWorkStat: {
        managerCount: 0,
        resolvedComplaintsCount: 0,
        complaintResolvedRate: 0.0,
        solvedRepairsCount: 0,
        repairSolvedRate: 0.0,
        publishedAnnouncementCount: 0
    },
    incomeStat: {
        total: 0.0,
        paymentTypeStatList: [
            {
                paymentType: '',
                totalAmount: 0.0
            }
        ]
    },
    generateTime: ''
})

function getChartData(orgData) {
    data.value = orgData
    if (data.value !== null) {
        const option1 = init().option1
        const option2 = init().option2
        const option3 = init().option3
        const option4 = init().option4
        return {
            option1,
            option2,
            option3,
            option4
        }
    }
}

const getIncomeType = () => {
    const map = []

    data.value.incomeStat.paymentTypeStatList.forEach((item) => {
        map.push({
            name: item.paymentType,
            value: item.totalAmount
        })
    })

    return {map}
}

function init() {
    let incomeType = getIncomeType()
    // console.log(incomeType)
    const option1 = {
        tooltip: {
            trigger: 'item'
        },
        legend: {
            top: '1%',
            left: 'center'
        },
        series: [
            {
                name: '房产空置率',
                type: 'pie',
                avoidLabelOverlap: false,
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
                    {value: data.value.basicCommunityStat.emptyPropertyRate * 100, name: '空置率'},
                    {value: (1 - data.value.basicCommunityStat.emptyPropertyRate) * 100, name: '入住率'}
                ]
            }
        ]
    }

    const option2 = {
        tooltip: {
            trigger: 'item'
        },
        legend: {
            top: '1%',
            left: 'center'
        },
        series: [
            {
                name: '投诉解决率',
                type: 'pie',
                avoidLabelOverlap: false,
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
                    {value: data.value.managerWorkStat.complaintResolvedRate * 100,
                        name: '解决',
                        itemStyle: {color: '#5ab726'}},
                    {value: (1 - data.value.managerWorkStat.complaintResolvedRate) * 100,
                        name: '未解决',
                        itemStyle: {color: '#ff375f'}}
                ]
            }
        ]
    }

    const option3 = {
        tooltip: {
            trigger: 'item'
        },
        legend: {
            top: '1%',
            left: 'center'
        },
        series: [
            {
                name: '报修解决率',
                type: 'pie',
                avoidLabelOverlap: false,
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
                    {value: data.value.managerWorkStat.repairSolvedRate * 100,
                        name: '解决',
                        itemStyle: {color: '#5ab726'}},
                    {value: (1 - data.value.managerWorkStat.repairSolvedRate) * 100,
                        name: '未解决',
                        itemStyle: {color: '#ff375f'}}
                ]
            }
        ]
    }

    const option4 = {
        tooltip: {
            trigger: 'item'
        },
        legend: {
            top: '1%',
            left: 'center'
        },
        series: [
            {
                name: '物业收费类别占比',
                type: 'pie',
                avoidLabelOverlap: false,
                itemStyle: {
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
                        fontSize: 30,
                        fontWeight: 'bold'
                    }
                },
                labelLine: {
                    show: false
                },
                data: incomeType.map
            }
        ]
    }
    return {
        option1,
        option2,
        option3,
        option4
    }
}

export {getChartData}