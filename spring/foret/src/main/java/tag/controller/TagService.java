package tag.controller;

import java.util.List;

import foret.bean.ForetTagDTO;
import member.bean.MemberTagDTO;
import tag.bean.TagDTO;

public interface TagService {
	// 태그 정보 데이터
	public int tagWrite(TagDTO tagDTO);
	public int tagDelete(TagDTO tagDTO);
	public List<TagDTO> tagList();
	public List<TagDTO> tagRank(int rank);
	
	// 태그 아이디 가져오기
	public List<TagDTO> getTagId(List<TagDTO> list);
	
	// 멤버_태그
	public int memberTagWrite(List<MemberTagDTO> list);
	public int memberTagDelete(int id);
	
	// 포레_태그
	public int foretTagWrite(List<ForetTagDTO> list);
	public int foretTagDelete(int id);
}
