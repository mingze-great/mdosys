package com.mdosys.scheduler.plugin.datasource.clickhouse;

import com.mdosys.scheduler.spi.datasource.DataSourceChannel;

import org.junit.Assert;
import org.junit.Test;

public class ClickhouseDataSourceChannelFactoryTest {

    @Test
    public void testCreate() {
        ClickhouseDataSourceChannelFactory sourceChannelFactory = new ClickhouseDataSourceChannelFactory();
        DataSourceChannel dataSourceChannel = sourceChannelFactory.create();
        Assert.assertNotNull(dataSourceChannel);
    }
}
