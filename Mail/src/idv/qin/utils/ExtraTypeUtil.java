package idv.qin.utils;

import idv.qin.doamin.ExtraType;

public class ExtraTypeUtil {
	
	private ExtraTypeUtil(){}
	
	public static ExtraType parseExtraType(String path){
		String suffix = null;
		if(path == null || "".equals(path.trim())){
			return ExtraType.NONE;
		}
		
		suffix = path.substring(path.lastIndexOf("."));
		if(suffix == null || "".equals(suffix)){
			return ExtraType.NONE;
		}
		
		if(suffix.contains("jpg") || suffix.contains("JPG") || suffix.contains("jpeg")
				||suffix.contains("JPEG") || suffix.contains("png") || suffix.contains("PNG") ){
			return ExtraType.IMAGE;
		}
		if(suffix.contains("mp3") || suffix.contains("MP3") || suffix.contains("wmv")|| suffix.contains("WMV")){
			return ExtraType.AUDIO;
		}
		if(suffix.contains("avi") || suffix.contains("rmvb") || suffix.contains("rm")||suffix.contains("AVI") ){
			return ExtraType.VIDEO;
		}
		if(suffix.contains("zip") || suffix.contains("ZIP")){
			return ExtraType.ZIP;
		}
		if(suffix.contains("txt") || suffix.contains("java") || suffix.contains("cpp")){
			return ExtraType.TXT;
		}
		if(suffix.contains("exe") || suffix.contains("EXE")){
			return ExtraType.EXE;
		}
		return ExtraType.NONE;
	}

}
