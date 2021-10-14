package org.sheep.back.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//21-10-01 json울 받아오기 위한 객체
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
	private Snippet snippet;

	public Item() {
		// TODO Auto-generated constructor stub
	}

	public String getTitle() {
		return snippet.getTitle();
	}

	public String getDesription() {
		return snippet.getDescription();
	}

	public String getVideoId() {
		return snippet.getResourceId().getVideoId();
	}

	public String getThumbnail() {
		return snippet.getThumbnails().getMedium().getUrl();
	}

	public Snippet getSnippet() {
		return snippet;
	}

	public void setSnippet(Snippet snippet) {
		this.snippet = snippet;
	}
	
	public String getPublishedAt() {
		return snippet.getPublishedAt();
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Snippet {
		private String title, description,publishedAt;
		private ResourceId resourceId;
		private Thumbnail thumbnails;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			//한줄 씩 읽기
			String[] sArr = this.description.split("\n");
			//스트링 조합을 위한 스트링 빌더 선언
			StringBuilder sb = new StringBuilder();
			//재료인지 아닌지 판별
			boolean isIngredient=false;
			//재료 판별 시작을 위한 정규표현식
			String regexStart="[ A-Za-z0-9ㄱ-힣]*\\[(\\s*)재료(\\s*)\\][ A-Za-z0-9ㄱ-힣]*";
			//재료 판별을 끝을 위한 정규표현식
			String regexEnd="[ A-Za-z0-9ㄱ-힣]*\\[[가-힣|\\s]+법[ A-Za-z0-9ㄱ-힣|\\]]*";
			
			for(String line:sArr) {
				//[재료] line 이후
				if(isIngredient) {
					//[만드는 법] 일 경우 재료 종료
					if(line.matches(regexEnd)) break;
					//스프링 빌더에 라인 붙임
					sb.append(line);
					// \n 붙이기
					sb.append(System.getProperty("line.separator"));
				}
				//해당 줄에 "[재료]" 부분이 있으면 재료 시작을 true;
				if(line.matches(regexStart)) isIngredient=true;
			}
			//재료 내보내기
			return sb.toString().trim();
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public ResourceId getResourceId() {
			return resourceId;
		}

		public void setResourceId(ResourceId resourceId) {
			this.resourceId = resourceId;
		}

		public Thumbnail getThumbnails() {
			return thumbnails;
		}

		public void setThumbnails(Thumbnail thumbnails) {
			this.thumbnails = thumbnails;
		}
		
		public String getPublishedAt() {
			return publishedAt;
		}

		public void setPublishedAt(String publishedAt) {
			this.publishedAt=publishedAt;
		}


		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class ResourceId {
			private String videoId;

			public String getVideoId() {
				return videoId;
			}

			public void setVideoId(String videoId) {
				this.videoId = videoId;
			}

		}

		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class Thumbnail {
			private Medium medium;

			public Medium getMedium() {
				return medium;
			}

			public void setMedium(Medium medium) {
				this.medium = medium;
			}

			@JsonIgnoreProperties(ignoreUnknown = true)
			public static class Medium {
				private String url;

				public String getUrl() {
					return url;
				}

				public void setUrl(String url) {
					this.url = url;
				}

			}

		}

	}//Snippet end

}//Item end
