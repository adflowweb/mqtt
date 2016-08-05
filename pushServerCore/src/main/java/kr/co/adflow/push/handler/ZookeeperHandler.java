package kr.co.adflow.push.handler;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.base.Charsets;

@Service
public class ZookeeperHandler {

	private RetryPolicy retryPolicy = null;
	private CuratorFramework curatorFramework = null;
	private LeaderLatch leaderLatch = null;
	private boolean leaderCheck = false;

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ZookeeperHandler.class);

	/** The Constant CONFIG_PROPERTIES. */
	private static final String CONFIG_PROPERTIES = "/config.properties";

	/** The prop. */
	private static Properties prop = new Properties();

	static {
		try {
			prop.load(HAHandler.class.getResourceAsStream(CONFIG_PROPERTIES));
			logger.debug("속성값=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String zookeeperUrl = prop.getProperty("zookeeper.url");
	private String zookeeperNode = prop.getProperty("zookeeper.node");
	private String zookeeperId = prop.getProperty("zookeeper.id");

	@PostConstruct
	public void curatorStart() throws Exception {
		logger.debug("zookeeper curator 시작");
		retryPolicy = new ExponentialBackoffRetry(1000, 3);
		curatorFramework = CuratorFrameworkFactory.newClient(zookeeperUrl, retryPolicy);

		logger.debug(zookeeperUrl);
		logger.debug(zookeeperNode);
		logger.debug(zookeeperId);

		curatorFramework.start();

		leaderLatch = new LeaderLatch(curatorFramework, zookeeperNode, zookeeperId);

		leaderLatch.addListener(new LeaderLatchListener() {

			@Override
			public void notLeader() {
				// TODO Auto-generated method stub
				logger.debug("리더가 아닙니다");
				setLeaderCheck(false);

			}

			@Override
			public void isLeader() {
				// TODO Auto-generated method stub
				setLeaderCheck(true);
				logger.debug("리더 입니다");
			}
		});

		try {
			leaderLatch.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("zookeeper client 연결에 실패하였습니다.");
		}
		logger.debug("zookeeper curator 시작 끝");
	}

	@PreDestroy
	public void curatorClose() {

		try {
			leaderLatch.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		curatorFramework.close();
	}

	public boolean hasLeader() {

		return leaderLatch.hasLeadership();
	}

	public boolean getLeader() {
		if (leaderCheck) {
			logger.debug("리더체크 true");
		} else {
			logger.debug("리더체크 false");
		}

		return leaderCheck;
	}

	public void setLeaderCheck(boolean leaderCheck) {
		this.leaderCheck = leaderCheck;
	}

}
