package com.lambdaworks.redis.masterslave;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.Arrays;

import org.junit.Test;

import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.models.role.RedisInstance;

/**
 * @author Mark Paluch
 */
public class MasterSlaveUtilsTest {

    @Test
    public void isChangedShouldReturnFalse() throws Exception {

        RedisMasterSlaveNode master = new RedisMasterSlaveNode("host", 1234, RedisURI.create("host", 111),
                RedisInstance.Role.MASTER);
        RedisMasterSlaveNode slave = new RedisMasterSlaveNode("host", 234, RedisURI.create("host", 234),
                RedisInstance.Role.SLAVE);

        RedisMasterSlaveNode newmaster = new RedisMasterSlaveNode("host", 1234, RedisURI.create("host", 555),
                RedisInstance.Role.MASTER);
        RedisMasterSlaveNode newslave = new RedisMasterSlaveNode("host", 234, RedisURI.create("host", 666),
                RedisInstance.Role.SLAVE);

        assertThat(MasterSlaveUtils.isChanged(Arrays.asList(master, slave), Arrays.asList(newmaster, newslave))).isFalse();
        assertThat(MasterSlaveUtils.isChanged(Arrays.asList(slave, master), Arrays.asList(newmaster, newslave))).isFalse();

        assertThat(MasterSlaveUtils.isChanged(Arrays.asList(newmaster, newslave), Arrays.asList(master, slave))).isFalse();
        assertThat(MasterSlaveUtils.isChanged(Arrays.asList(newmaster, newslave), Arrays.asList(slave, master))).isFalse();
    }

    @Test
    public void isChangedShouldReturnTrueBecauseSlaveIsGone() throws Exception {

        RedisMasterSlaveNode master = new RedisMasterSlaveNode("host", 1234, RedisURI.create("host", 111),
                RedisInstance.Role.MASTER);
        RedisMasterSlaveNode slave = new RedisMasterSlaveNode("host", 234, RedisURI.create("host", 234),
                RedisInstance.Role.MASTER);

        RedisMasterSlaveNode newmaster = new RedisMasterSlaveNode("host", 1234, RedisURI.create("host", 111),
                RedisInstance.Role.MASTER);

        assertThat(MasterSlaveUtils.isChanged(Arrays.asList(master, slave), Arrays.asList(newmaster))).isTrue();
    }

    @Test
    public void isChangedShouldReturnTrueBecauseHostWasMigrated() throws Exception {

        RedisMasterSlaveNode master = new RedisMasterSlaveNode("host", 1234, RedisURI.create("host", 111),
                RedisInstance.Role.MASTER);
        RedisMasterSlaveNode slave = new RedisMasterSlaveNode("host", 234, RedisURI.create("host", 234),
                RedisInstance.Role.SLAVE);

        RedisMasterSlaveNode newmaster = new RedisMasterSlaveNode("host", 1234, RedisURI.create("host", 555),
                RedisInstance.Role.MASTER);
        RedisMasterSlaveNode newslave = new RedisMasterSlaveNode("newhost", 234, RedisURI.create("newhost", 666),
                RedisInstance.Role.SLAVE);

        assertThat(MasterSlaveUtils.isChanged(Arrays.asList(master, slave), Arrays.asList(newmaster, newslave))).isTrue();
        assertThat(MasterSlaveUtils.isChanged(Arrays.asList(slave, master), Arrays.asList(newmaster, newslave))).isTrue();
        assertThat(MasterSlaveUtils.isChanged(Arrays.asList(newmaster, newslave), Arrays.asList(master, slave))).isTrue();
        assertThat(MasterSlaveUtils.isChanged(Arrays.asList(newslave, newmaster), Arrays.asList(master, slave))).isTrue();
    }

    @Test
    public void isChangedShouldReturnTrueBecauseRolesSwitched() throws Exception {

        RedisMasterSlaveNode master = new RedisMasterSlaveNode("host", 1234, RedisURI.create("host", 111),
                RedisInstance.Role.MASTER);
        RedisMasterSlaveNode slave = new RedisMasterSlaveNode("host", 234, RedisURI.create("host", 234),
                RedisInstance.Role.MASTER);

        RedisMasterSlaveNode newslave = new RedisMasterSlaveNode("host", 1234, RedisURI.create("host", 111),
                RedisInstance.Role.SLAVE);
        RedisMasterSlaveNode newmaster = new RedisMasterSlaveNode("host", 234, RedisURI.create("host", 234),
                RedisInstance.Role.MASTER);

        assertThat(MasterSlaveUtils.isChanged(Arrays.asList(master, slave), Arrays.asList(newmaster, newslave))).isTrue();
        assertThat(MasterSlaveUtils.isChanged(Arrays.asList(master, slave), Arrays.asList(newslave, newmaster))).isTrue();
    }
}