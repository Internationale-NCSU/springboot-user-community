package com.dareway.jc.content.community.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus配置
 *
 * @author lichp
 * @version 1.0.0  2020/10/22 9:47
 * @since JDK1.8
 */
@Configuration
public class MybatisPlusConfig {


    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(this.paginationInterceptor());
        interceptor.addInnerInterceptor(this.blockAttackInnerInterceptor());
        return interceptor;
    }

    /**
     *  需配置该值为false,避免1或2级缓存可能出现问题,该属性会在旧插件移除后一同移除
     * @return com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer
     * @author  lichp
     * @version 1.0.0  2020/12/30 9:48
     * @since JDK1.8
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }

    /**
     * overflow	boolean	false	溢出总页数后是否进行处理(默认不处理,参见 插件#continuePage 方法)
     * maxLimit	Long		单页分页条数限制(默认无限制,参见 插件#handlerLimit 方法)
     * dbType	DbType		数据库类型(根据类型获取应使用的分页方言,参见 插件#findIDialect 方法)
     * dialect	IDialect		方言实现类(参见 插件#findIDialect 方法)
     * 默认对 left join 进行优化,虽然能优化count,但是加上分页的话如果1对多本身结果条数就是不正确的
     * @return com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
     * @author  lichp
     * @version 1.0.0  2020/12/30 9:30
     * @since JDK1.8
     */
    private PaginationInnerInterceptor paginationInterceptor() {
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        paginationInterceptor.setOverflow(false);
        paginationInterceptor.setMaxLimit(500L);
        paginationInterceptor.setDbType(DbType.POSTGRE_SQL);
        return paginationInterceptor;
    }

    /**
     * 针对 update 和 delete 语句 作用: 阻止恶意的全表更新删除
     * @return com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor
     * @author  lichp
     * @version 1.0.0  2020/12/30 10:07
     * @since JDK1.8
     */
    private BlockAttackInnerInterceptor blockAttackInnerInterceptor(){
        return new BlockAttackInnerInterceptor();
    }
}
