package kh.init.feeds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kh.init.members.MemberDTO;

@Repository
public class FeedDAO {

	@Autowired
	private SqlSessionTemplate jdbc;
	
	public int getFeedSeq() throws Exception{
		int feed_seq = jdbc.selectOne("Feed.getFeedSeq");
		return feed_seq;
	}

	
	public List<FeedDTO> getMyFeed(String email) throws Exception{
		List<FeedDTO> list = jdbc.selectList("Feed.getMyFeed", email);
		return list;
	}
	
	//wholeFeed에서 해시태그 검색 또는 그냥 기본wholeFeed뽑을때 
	public List<FeedDTO> selectAll(String keyword) throws Exception{
		List<FeedDTO> list = jdbc.selectList("Feed.selectAll", keyword);
		return list;
	}
	
	//wholeFeed에서 친구검색했을 경우
	public List<MemberDTO> searchFriend(String keyword) throws Exception{
		List<MemberDTO> list = jdbc.selectList("Feed.searchFriend", keyword);
		return list;
	}
	
	
	//writeFeed에서 글쓰기를 눌렀을때 내용등록
	public int registerFeed(FeedDTO dto) throws Exception{
		int result = jdbc.insert("Feed.registerFeed", dto);
		return result;
	}
	//writeFeed에서 글쓰기 눌렀을때 미디어 등록
	public int registerMedia(int feed_seq, String media) throws Exception{
		Map<String, String> param = new HashMap<>();
		param.put("feed_seq", feed_seq+"");
		param.put("media", media);
		int result = jdbc.insert("Feed.registerMedia", param);
		return result;
	}
	
	public int deleteFeed(int seq) throws Exception{
		return jdbc.delete("Feed.deleteFeed", seq);
	}

	public List<FeedDTO> scrapFeed(String email) throws Exception{
		List<FeedDTO> list = jdbc.selectList("Feed.scrapFeed", email);
		return list;
	}


	
	
	public int modifyFeed(FeedDTO dto)throws Exception{
		return jdbc.update("Feed.modifyFeed",dto);		
	}
	
	public FeedDTO detailView(int feed_seq) throws Exception{
		FeedDTO dto = jdbc.selectOne("Feed.detailView", feed_seq);
		return dto;
	}
	
	//controller-detailView에서 media 목록을 얻기 위한 dao
	public List<String> getMediaList(int feed_seq) throws Exception{
		List<String> list = jdbc.selectList("Feed.getMediaList", feed_seq);
		return list;
	}
	
	//detailView 열때 좋아요체크
	public int likeCheck(int feed_seq, String email) throws Exception{
		Map<String, String> param = new HashMap<>();
		param.put("feed_seq", feed_seq+"");
		param.put("email", email);
		int result = jdbc.selectOne("Feed.likeCheck", param);
		return result;
	}
	
	//detailView 열때 북마크체크
	public int bookmarkCheck(int feed_seq, String email) throws Exception{
		Map<String, String> param = new HashMap<>();
		param.put("feed_seq", feed_seq+"");
		param.put("email", email);
		int result = jdbc.selectOne("Feed.bookmarkCheck", param);
		return result;
	}
	
	public int getLikeSeq() throws Exception{
		int like_seq = jdbc.selectOne("Feed.getLikeSeq");
		return like_seq;
	}
	
	//좋아요
	public int insertLike(int like_seq, int feed_seq, String email) throws Exception{
		Map<String, String> param = new HashMap<>();
		param.put("like_seq", like_seq+"");
		param.put("feed_seq", feed_seq+"");
		param.put("email", email);
		int result = jdbc.insert("Feed.insertLike", param);
		return result;
	}
	public int deleteLike(int feed_seq, String email) throws Exception{
		Map<String, String> param = new HashMap<>();
		param.put("feed_seq", feed_seq+"");
		param.put("email", email);
		int result = jdbc.insert("Feed.deleteLike", param);
		return result;
	}
	
	//북마크
	public int insertBookmark(int feed_seq, String email) throws Exception{
		Map<String, String> param = new HashMap<>();
		param.put("feed_seq", feed_seq+"");
		param.put("email", email);
		int result = jdbc.insert("Feed.insertBookmark", param);
		return result;
	}
	public int deleteBookmark(int feed_seq, String email) throws Exception{
		Map<String, String> param = new HashMap<>();
		param.put("feed_seq", feed_seq+"");
		param.put("email", email);
		int result = jdbc.insert("Feed.deleteBookmark", param);
		return result;
	}
}
