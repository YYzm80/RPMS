package com.example.mapper;

import com.example.entity.dto.stat.DailyWorkStat;
import com.example.entity.dto.stat.PaymentTypeStat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface StaticMapper {

    @Select("""
                    -- 统计近7天每日的报修完成量和投诉解决量
                    SELECT
                        dates.date AS `date`,
                        IFNULL(SUM(repair_completed), 0) AS repair_completed_count,
                        IFNULL(SUM(complaint_resolved), 0) AS complaint_resolved_count
                    FROM (
                        -- 生成最近7天的日期序列（包含今天，但数据从昨天开始）
                        SELECT CURDATE() - INTERVAL (a.a + (10 * b.a)) DAY AS date
                        FROM
                            (SELECT 0 AS a UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) AS a
                            CROSS JOIN (SELECT 0 AS a UNION ALL SELECT 1) AS b
                        WHERE (a.a + (10 * b.a)) BETWEEN 1 AND 7  -- 从昨天开始往前7天
                    ) dates
                    LEFT JOIN (
                        -- 报修完成量统计
                        SELECT
                            DATE(completion_time) AS completion_date,
                            COUNT(*) AS repair_completed
                        FROM repair
                        WHERE
                            status = 'completed'
                            AND completion_time BETWEEN CURDATE() - INTERVAL 7 DAY AND CURDATE()
                        GROUP BY DATE(completion_time)
                    ) repairs ON dates.date = repairs.completion_date
                    LEFT JOIN (
                        -- 投诉解决量统计
                        SELECT
                            DATE(handle_time) AS handle_date,
                            COUNT(*) AS complaint_resolved
                        FROM complaint
                        WHERE
                            status = 'resolved'
                            AND handle_time BETWEEN CURDATE() - INTERVAL 7 DAY AND CURDATE()
                        GROUP BY DATE(handle_time)
                    ) complaints ON dates.date = complaints.handle_date
                    GROUP BY dates.date
                    ORDER BY dates.date DESC;
            """)
    List<DailyWorkStat> getDailyWorkStat();

    @Select("""
            SELECT
                type AS payment_type,
                SUM(amount) AS total_amount
            FROM payment
            WHERE status = 'paid'
                AND payment_time >= DATE_SUB(NOW(), INTERVAL 1 YEAR)
            GROUP BY type
            """)
    List<PaymentTypeStat> getPaymentTypeYearStat();

    @Select("""
            SELECT
                type AS payment_type,
                SUM(amount) AS total_amount
            FROM payment
            WHERE status = 'paid'
            AND payment_time >= DATE_FORMAT(#{month}, '%Y-%m-01')
            AND payment_time < DATE_ADD(DATE_FORMAT(#{month}, '%Y-%m-01'), INTERVAL 1 MONTH)
            GROUP BY type
            """)
    List<PaymentTypeStat> getPaymentTypeMonthStat(Date month);
}