import { ref, computed, watchEffect } from 'vue';
import { useTransition } from '@vueuse/core';

/**
 * 搜索和分页
 * @param tableData 表格数据
 * @param pageSize 每页显示的记录数
 * @param searchFields 搜索字段
 * @returns {{pagedData: *, highlight: (function(*, *): (*)), search: *, total: *, initData: (function(): void), updatePageData: (function(): void), pageSize, filteredData: *, currentPage: *, handlePageChange: (function(*): void)}}
 */
export function useSearchAndPagination(tableData, pageSize, searchFields = ['type']) {
    const search = ref('');

    const highlight = (text, keyword) => {
        if (!keyword.trim()) return text; // 无关键词时返回原文

        // 转义正则特殊字符
        const escapedKeyword = keyword.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
        const regex = new RegExp(`(${escapedKeyword})`, 'gi'); // 全局+忽略大小写

        return text.replace(regex, '<mark>$1</mark>');
    };

    const filteredData = computed(() => {
        const searchLower = search.value.toLowerCase(); // 将搜索词转换为小写
        return tableData.value.filter((item) => {
            return searchFields.some(field =>
                item[field] && item[field].toLowerCase().indexOf(searchLower) !== -1
            );
        });
    });

    const currentPage = ref(1); // 当前页码

    const pagedData = computed(() => {
        const start = (currentPage.value - 1) * pageSize;
        return filteredData.value.slice(start, start + pageSize);
    });

    const total = computed(() => filteredData.value.length);

    // 初始化数据
    const initData = () => {
        total.value = tableData.value.length;
        updatePageData();
    };

    // 更新当前页的数据
    const updatePageData = () => {
        const start = (currentPage.value - 1) * pageSize;
        const end = start + pageSize;
        pagedData.value = filteredData.value.slice(start, end);
    };

    // 处理页码变化的函数
    const handlePageChange = (newPage) => {
        currentPage.value = newPage;
        updatePageData();
    };

    return {
        search,
        highlight,
        filteredData,
        pageSize,
        currentPage,
        pagedData,
        total,
        initData,
        updatePageData,
        handlePageChange,
    };
}

/**
 * 动态过渡
 * @param targetValue 目标值
 * @param duration 过渡持续时间（毫秒）
 * @returns {*}
 */
export function useDynamicTransition(targetValue, duration = 1500) {
    const transitionValue = ref(0)
    const transition = useTransition(transitionValue, { duration })

    watchEffect(() => {
        transitionValue.value = targetValue.value
    })

    return  computed(() => Math.round(transition.value))
}

