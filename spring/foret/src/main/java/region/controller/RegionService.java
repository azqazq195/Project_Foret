package region.controller;

import java.util.List;

import foret.bean.ForetRegionDTO;
import member.bean.MemberRegionDTO;
import region.bean.RegionDTO;

public interface RegionService {
	// 지역 정보 데이터
	public int regionWrite(RegionDTO regionDTO);
	public int regionDelete(RegionDTO regionDTO);
	public List<RegionDTO> regionList();
	public List<RegionDTO> regionRank(int rank);
	
	
	// 지역 아이디 가져오기
	public List<RegionDTO> getRegionId(List<RegionDTO> list);
	
	// 맴버_지역
	public int memberRegionWrite(List<MemberRegionDTO> list);
	public int memberRegionDelete(int id);
	
	// 포레_지역
	public int foretRegionWrite(List<ForetRegionDTO> list);
	public int foretRegionDelete(int id);
}
