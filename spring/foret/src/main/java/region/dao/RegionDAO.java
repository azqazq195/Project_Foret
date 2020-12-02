package region.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import foret.bean.ForetRegionDTO;
import member.bean.MemberRegionDTO;
import region.bean.RegionDTO;

@Repository
public class RegionDAO {
	@Autowired
	SqlSessionTemplate sqlSession;
	
	public int regionWrite(RegionDTO regionDTO) {
		return sqlSession.insert("mybatis.regionMapper.regionWrite", regionDTO);
	}
	public int regionDelete(RegionDTO regionDTO) {
		return sqlSession.delete("mybatis.regionMapper.regionDelete", regionDTO);
	}
	public List<RegionDTO> regionList() {
		return sqlSession.selectList("mybatis.regionMapper.regionList");
	}
	public List<RegionDTO> regionRank() {
		return sqlSession.selectList("mybatis.regionMapper.regionRank");
	}
	
	public List<RegionDTO> getRegionId(List<RegionDTO> list) {
		return sqlSession.selectList("mybatis.regionMapper.getRegionId", list);
	}
	
	public int memberRegionWrite(List<MemberRegionDTO> list) {
		return sqlSession.insert("mybatis.regionMapper.memberRegionWrite", list);
	}
	public int memberRegionDelete(int id) {
		return sqlSession.insert("mybatis.regionMapper.memberRegionDelete", id);
	}
	
	public int foretRegionWrite(List<ForetRegionDTO> list) {
		return sqlSession.insert("mybatis.regionMapper.foretRegionWrite", list);
	}
	public int foretRegionDelete(int id) {
		return sqlSession.insert("mybatis.regionMapper.foretRegionDelete", id);
	}
}
