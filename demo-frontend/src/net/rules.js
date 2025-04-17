export const contentRules = {
    content: [
        {required: true, message: '内容不能为空', trigger: ['blur', 'change']},
    ],
}

export const descriptionRules = {
    description: [
        {required: true, message: '内容不能为空', trigger: ['blur', 'change']},
    ],
}

export const userRules = {
    username: [
        {required: true, message: '用户名不能为空', trigger: 'blur'},
        {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: ['blur', 'change']},
    ],
    gender: [
        {required: true, message: '性别不能为空', trigger: 'blur'},
    ],
    address: [
        {required: true, message: '邮箱地址不能为空', trigger: 'blur'},
        {type: 'email', message: '邮箱格式不正确', trigger: ['blur', 'change']}
    ],
    realName: [
        {required: true, message: '姓名不能为空', trigger: 'blur'},
        {min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: ['blur', 'change']},
    ],
    phone: [
        {required: true, message: '手机号不能为空', trigger: 'blur'},
        {pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: ['blur', 'change']},
    ],
    rid: [
        {required: true, message: '角色不能为空', trigger: 'blur'},
    ],
    position: [
        {required: true, message: '姓名不能为空', trigger: ['blur', 'change']},
    ],
    hireDate: [
        {required: true, message: '入职日期不能为空', trigger: 'blur'},
    ]
}

export const propertyRules = {
    floorArea: [
        {required: true, message: '面积不能为空', trigger: 'blur'},
        {pattern: /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0)$)|(^[0-9]\.[0-9]([0-9])?$)/, message: '面积必须为浮点数，小数点两位', trigger: ['blur', 'change']},
    ],
    buildingNumber: [
        {required: true, message: '楼栋号不能为空', trigger: 'blur'},
        {type: 'number', message: '楼栋号必须为数字', trigger: ['blur', 'change']},
    ],
    roomNumber: [
        {required: true, message: '房号不能为空', trigger: 'blur'},
        {type: 'number', message: '房号必须为数字', trigger: ['blur', 'change']},
    ],
    purchaseDate: [
        {required: true, message: '购买日期不能为空', trigger: 'blur'},
    ]
}

export const announcementRules = {
    title: [
        {required: true, message: '标题不能为空', trigger: 'blur'},
        {min: 2, max: 30, message: '长度在 2 到 30 个字符',}
    ],
    content: [
        {required: true, message: '内容不能为空', trigger: ['blur', 'change']},
    ]
}

export const paymentRules = {
    userIds: [
        {required: true, message: '缴费人不能为空', trigger: ['blur', 'change']},
    ],
    amount: [
        {required: true, message: '费用金额不能为空', trigger: 'blur'},
        {pattern: /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0)$)|(^[0-9]\.[0-9]([0-9])?$)/, message: '金额必须为浮点数，小数点两位', trigger: ['blur', 'change']},
    ],
    type: [
        {required: true, message: '费用类型不能为空', trigger: 'blur'}
    ]
}