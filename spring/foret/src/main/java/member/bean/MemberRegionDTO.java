package member.bean;

public class MemberRegionDTO {
	private int id;
	private int region_id;
	private String region_si;
	private String region_gu;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRegion_id() {
		return region_id;
	}
	public void setRegion_id(int region_id) {
		this.region_id = region_id;
	}
	public String getRegion_si() {
		return region_si;
	}
	public void setRegion_si(String region_si) {
		this.region_si = region_si;
	}
	public String getRegion_gu() {
		return region_gu;
	}
	public void setRegion_gu(String region_gu) {
		this.region_gu = region_gu;
	}
}

