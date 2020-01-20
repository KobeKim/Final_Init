package kh.init.alarm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kh.init.feeds.ReplyDTO;
import kh.init.friends.FriendRequestDTO;

@Service
public class AlarmService {
	
	@Autowired
	private AlarmDAO dao;
	
//	@Transactional("txManager")
//	public List<AlarmDTO> alarmList(String email){
//		return dao.alarmList(email);
//	}
	
	@Transactional("txManager")
	public List<AlarmVO> alarmList(String email){
		return dao.alarmList(email);
	}
	
	@Transactional("txManager")
	public String alarmWho(int feed_seq){
		return dao.alarmWho(feed_seq);
	}
	
	@Transactional("txManager")
	public int deleteAlarm(String email, int alarm_seq) {
		return dao.deleteAlarm(email, alarm_seq);
	}
	
	@Transactional("txManager")
	public int alarmCheck(String email) {
		return dao.alarmCheck(email);
	}
	
	@Transactional("txManager")
	public int isNewAlarm(String email) {
		return dao.isNewAlarm(email);
	}
	
	@Transactional("txManager")
	public int alarmFriend(int sharedSeq, FriendRequestDTO dto) {
		return dao.alarmFriend(sharedSeq, dto);
	}
	
	

}
