package tag.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import foret.bean.ForetTagDTO;
import member.bean.MemberTagDTO;
import tag.bean.TagDTO;
import tag.dao.TagDAO;

@Service
public class TagServiceImpl implements TagService{

	@Autowired
	TagDAO tagDAO;
	
	@Override
	public int tagWrite(TagDTO tagDTO) {
		return tagDAO.tagWrite(tagDTO);
	}

	@Override
	public int tagDelete(TagDTO tagDTO) {
		return tagDAO.tagDelete(tagDTO);
	}
	
	@Override
	public List<TagDTO> tagList() {
		return tagDAO.tagList();
	}

	@Override
	public List<TagDTO> getTagId(List<TagDTO> list) {
		return tagDAO.getTagId(list);
	}

	@Override
	public int memberTagWrite(List<MemberTagDTO> list) {
		return tagDAO.memberTagWrite(list);
	}

	@Override
	public int memberTagDelete(int id) {
		return tagDAO.memberTagDelete(id);
	}

	@Override
	public int foretTagWrite(List<ForetTagDTO> list) {
		return tagDAO.foretTagWrite(list);
	}

	@Override
	public int foretTagDelete(int id) {
		return tagDAO.foretTagDelete(id);
	}

}
