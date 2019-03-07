package sports;

public class ClientDTO {

	private ClientType clientType;
	private String userName;
	private String password;

	public ClientDTO() {
		super();
	}

	public ClientDTO(ClientType clientType, String userName, String password) {
		super();
		this.clientType = clientType;
		this.userName = userName;
		this.password = password;
	}

	public ClientType getClientType() {
		return clientType;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "ClientDTO [clientType=" + clientType + ", userName=" + userName + ", password=" + password + "]";
	}

}
