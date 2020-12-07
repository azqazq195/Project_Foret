package region.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import foret.bean.ForetRegionDTO;
import member.bean.MemberRegionDTO;
import region.bean.RegionDTO;
import region.dao.RegionDAO;


@Service
public class RegionServiceImpl implements RegionService{
	
	@Autowired
	RegionDAO regionDAO;

	@Override
	public int regionWrite(RegionDTO regionDTO) {
		return regionDAO.regionWrite(regionDTO);
	}

	@Override
	public int regionDelete(RegionDTO regionDTO) {
		return regionDAO.regionDelete(regionDTO);
	}

	@Override
	public List<RegionDTO> regionList() {
		return regionDAO.regionList();
	}

	@Override
	public List<RegionDTO> getRegionId(List<RegionDTO> list) {
		return regionDAO.getRegionId(list);
	}

	@Override
	public int memberRegionWrite(List<MemberRegionDTO> list) {
		return regionDAO.memberRegionWrite(list);
	}

	@Override
	public int memberRegionDelete(int id) {
		return regionDAO.memberRegionDelete(id);
	}

	@Override
	public int foretRegionWrite(List<ForetRegionDTO> list) {
		return regionDAO.foretRegionWrite(list);
	}

	@Override
	public int foretRegionDelete(int id) {
		return regionDAO.foretRegionDelete(id);
	}

	@Override
	public List<RegionDTO> regionRank(int rank) {
		return regionDAO.regionRank(rank);
	}


}
