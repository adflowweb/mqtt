package kr.co.adflow.pms.core.handler;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.adflow.pms.core.config.PmsConfig;

@Service
public class ZookeeperHandler {

	private RetryPolicy retryPolicy = null;
	private CuratorFramework curatorFramework = null;
	private LeaderLatch leaderLatch = null;
	private boolean leaderCheck = false;
	@Autowired
	private PmsConfig pmsConfig;

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ZookeeperHandler.class);

	@PostConstruct
	public void curatorStart() {
		logger.debug("zookeeper curator 시작");
		retryPolicy = new ExponentialBackoffRetry(1000, 3);
		curatorFramework = CuratorFrameworkFactory.newClient(pmsConfig.ZOOKEEPER_URL, retryPolicy);
		curatorFramework.start();
		leaderLatch = new LeaderLatch(curatorFramework, pmsConfig.ZOOKEEPER_NODE, pmsConfig.ZOOKEEPER_ID);

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
		curatorFramework.close();
		try {
			leaderLatch.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
