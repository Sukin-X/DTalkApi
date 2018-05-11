package cn.jlhd.util;

import java.io.IOException;

public class HttpConnectException extends IOException {
	private static final long serialVersionUID = 1L;

	public HttpConnectException(String message) {
		super(message);
	}

	public HttpConnectException(Throwable cause) {
		super(cause);
	}

	public static class UnauthorizedException extends HttpConnectException {
		private static final long serialVersionUID = 1L;

		public UnauthorizedException(String message) {
			super(message);
		}
	}

	public static class ForbiddenException extends HttpConnectException {
		private static final long serialVersionUID = 1L;

		public ForbiddenException(String message) {
			super(message);
		}
	}

	public static class NotAllowedException extends HttpConnectException {
		private static final long serialVersionUID = 1L;

		public NotAllowedException(String message) {
			super(message);
		}
	}

	public static class UnknownRequestException extends HttpConnectException {
		private static final long serialVersionUID = 1L;

		public UnknownRequestException(String message) {
			super(message);
		}
	}
}