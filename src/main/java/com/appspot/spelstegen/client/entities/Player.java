package com.appspot.spelstegen.client.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Class representing a player
 * 
 * @author Henrik Segesten
 * @author Per Mattsson
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Player implements Serializable/*, Comparable<Player>*/ {
	
	/**
	 * Describes a players role in a league.
	 * NON_MEMBER = player is not a member of this league.
	 * MEMBER = ordinary player with no extra rights.
	 * MATCH_ADMIN = the player is allowed to administer matches.
	 * LEAGUE_ADMIN = the player is allowed to add and remove players from the league.
	 * SUPER_USER = the player is both a league admin and match admin. 
	 */
	public enum LeagueRole {NON_MEMBER, MEMBER, MATCH_ADMIN, LEAGUE_ADMIN, SUPER_USER}
	
	private static final long serialVersionUID = 1L;
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Integer id;
	
	/** 
	 * A map that contains all roles that the player has in a set of leagues. 
	 */
	@Persistent
	private Map<Long, java.util.Set<LeagueRole>> allLeagueRoles;
	
	@Persistent
	private String playerName;
	
	@Persistent
	private String email;
	
	@Persistent
	private String password;
	
	@Persistent
	private String nickName;
	
	@Persistent
	private String imageURL;
	
	public Player() {}
	
	public Player(String playerName, String email) {
		this.playerName = playerName;
		this.email = email;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public void changePassword(String newPassword) {
		password = md5(newPassword);
	}
	
	public void setEncryptedPassword(String password) {
		this.password = password;
	}
	
	public String getEncryptedPassword() {
		return password;
	}
	
	public Map<Long, java.util.Set<LeagueRole>> getAllLeagueRoles() {
		if (allLeagueRoles == null) {
			allLeagueRoles = new HashMap<Long, java.util.Set<LeagueRole>>();
		}
		return allLeagueRoles;
	}

	public void setAllLeagueRoles(Map<Long, java.util.Set<LeagueRole>> allLeagueRoles) {
		this.allLeagueRoles = allLeagueRoles;
	}
	
	/**
	 * Returns a set of all league roles that the player has for a certain
	 * league
	 * 
	 * @param leagueId
	 *            id of league to check roles for
	 * @return a set of all league roles that the player has for the league. If
	 *         no roles are available an empty set is returned.
	 */
	public java.util.Set<LeagueRole> getLeagueRoles(Long leagueId) {
		java.util.Set<LeagueRole> leagueRoles = getAllLeagueRoles().get(leagueId);
		if (leagueRoles == null) {
			leagueRoles = new HashSet<LeagueRole>();
		}
		return leagueRoles;
	}
	
	public void setLeagueRoles(Long leagueId, java.util.Set<LeagueRole> leagueRoles) {
		getAllLeagueRoles().put(leagueId, leagueRoles);
	}

	
	public boolean isMatchAdmin(Long leagueId) {
		return getLeagueRoles(leagueId).contains(LeagueRole.MATCH_ADMIN);
	}
	
	public boolean isLeagueAdmin(Long leagueId) {
		return getLeagueRoles(leagueId).contains(LeagueRole.LEAGUE_ADMIN);
	}
	
	public boolean isLeagueMember(Long leagueId) {
		return getLeagueRoles(leagueId).contains(LeagueRole.MEMBER);
	}
	
	/**
	 * Returns a list with all league ids where the player has a specific role
	 * 
	 * @param leagueRole
	 *            the league role
	 */
	public List<Long> getLeagueIds(LeagueRole leagueRole) {
		List<Long> leagueIds = new ArrayList<Long>();
		for (Entry<Long, Set<LeagueRole>> leagueUserRoles : getAllLeagueRoles().entrySet()) {
			if (leagueUserRoles.getValue().contains(leagueRole)) {
				leagueIds.add(leagueUserRoles.getKey());
			}
		}
		return leagueIds;
	}

	public String toString() {
		return "Id: " + id + ", namn: " + playerName + ", epost: " + email + 
				", smeknamn: " + nickName + ", bildURL: " + imageURL;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * Javascript implementation of md5 hash.
	 * @param string
	 * @return
	 */
	public static native String md5(String string) /*-{ 
    function RotateLeft(lValue, iShiftBits) { 
    return (lValue<<iShiftBits) | (lValue>>>(32-iShiftBits)); 
    } 

    function AddUnsigned(lX,lY) { 
    var lX4,lY4,lX8,lY8,lResult; 
    lX8 = (lX & 0x80000000); 
    lY8 = (lY & 0x80000000); 
    lX4 = (lX & 0x40000000); 
    lY4 = (lY & 0x40000000); 
    lResult = (lX & 0x3FFFFFFF)+(lY & 0x3FFFFFFF); 
    if (lX4 & lY4) { 
    return (lResult ^ 0x80000000 ^ lX8 ^ lY8); 
    } 
    if (lX4 | lY4) { 
    if (lResult & 0x40000000) { 
    return (lResult ^ 0xC0000000 ^ lX8 ^ lY8); 
    } else { 
    return (lResult ^ 0x40000000 ^ lX8 ^ lY8); 
    } 
    } else { 
    return (lResult ^ lX8 ^ lY8); 
    } 
    } 

    function F(x,y,z) { return (x & y) | ((~x) & z); } 
    function G(x,y,z) { return (x & z) | (y & (~z)); } 
    function H(x,y,z) { return (x ^ y ^ z); } 
    function I(x,y,z) { return (y ^ (x | (~z))); } 

    function FF(a,b,c,d,x,s,ac) { 
    a = AddUnsigned(a, AddUnsigned(AddUnsigned(F(b, c, d), x), ac)); 
    return AddUnsigned(RotateLeft(a, s), b); 
    }; 

    function GG(a,b,c,d,x,s,ac) { 
    a = AddUnsigned(a, AddUnsigned(AddUnsigned(G(b, c, d), x), ac)); 
    return AddUnsigned(RotateLeft(a, s), b); 
    }; 

    function HH(a,b,c,d,x,s,ac) { 
    a = AddUnsigned(a, AddUnsigned(AddUnsigned(H(b, c, d), x), ac)); 
    return AddUnsigned(RotateLeft(a, s), b); 
    }; 

    function II(a,b,c,d,x,s,ac) { 
    a = AddUnsigned(a, AddUnsigned(AddUnsigned(I(b, c, d), x), ac)); 
    return AddUnsigned(RotateLeft(a, s), b); 
    }; 

    function ConvertToWordArray(string) { 
    var lWordCount; 
    var lMessageLength = string.length; 
    var lNumberOfWords_temp1=lMessageLength + 8; 
    var lNumberOfWords_temp2=(lNumberOfWords_temp1-(lNumberOfWords_temp1 
% 64))/64; 
    var lNumberOfWords = (lNumberOfWords_temp2+1)*16; 
    var lWordArray=Array(lNumberOfWords-1); 
    var lBytePosition = 0; 
    var lByteCount = 0; 
    while ( lByteCount < lMessageLength ) { 
    lWordCount = (lByteCount-(lByteCount % 4))/4; 
    lBytePosition = (lByteCount % 4)*8; 
    lWordArray[lWordCount] = (lWordArray[lWordCount] | 
(string.charCodeAt(lByteCount)<<lBytePosition)); 
    lByteCount++; 
    } 
    lWordCount = (lByteCount-(lByteCount % 4))/4; 
    lBytePosition = (lByteCount % 4)*8; 
    lWordArray[lWordCount] = lWordArray[lWordCount] | 
(0x80<<lBytePosition); 
    lWordArray[lNumberOfWords-2] = lMessageLength<<3; 
    lWordArray[lNumberOfWords-1] = lMessageLength>>>29; 
    return lWordArray; 
    }; 

    function WordToHex(lValue) { 
    var WordToHexValue="",WordToHexValue_temp="",lByte,lCount; 
    for (lCount = 0;lCount<=3;lCount++) { 
    lByte = (lValue>>>(lCount*8)) & 255; 
    WordToHexValue_temp = "0" + lByte.toString(16); 
    WordToHexValue = WordToHexValue + 
WordToHexValue_temp.substr(WordToHexValue_temp.length-2,2); 
    } 
    return WordToHexValue; 
    }; 

    function Utf8Encode(string) { 
    string = string.replace(/\r\n/g,"\n"); 
    var utftext = ""; 

    for (var n = 0; n < string.length; n++) { 

    var c = string.charCodeAt(n); 

    if (c < 128) { 
    utftext += String.fromCharCode(c); 
    } 
    else if((c > 127) && (c < 2048)) { 
    utftext += String.fromCharCode((c >> 6) | 192); 
    utftext += String.fromCharCode((c & 63) | 128); 
    } 
    else { 
    utftext += String.fromCharCode((c >> 12) | 224); 
    utftext += String.fromCharCode(((c >> 6) & 63) | 128); 
    utftext += String.fromCharCode((c & 63) | 128); 
    } 

    } 

    return utftext; 
    }; 

    var x=Array(); 
    var k,AA,BB,CC,DD,a,b,c,d; 
    var S11=7, S12=12, S13=17, S14=22; 
    var S21=5, S22=9 , S23=14, S24=20; 
    var S31=4, S32=11, S33=16, S34=23; 
    var S41=6, S42=10, S43=15, S44=21; 

    string = Utf8Encode(string); 

    x = ConvertToWordArray(string); 

    a = 0x67452301; b = 0xEFCDAB89; c = 0x98BADCFE; d = 0x10325476; 

    for (k=0;k<x.length;k+=16) { 
    AA=a; BB=b; CC=c; DD=d; 
    a=FF(a,b,c,d,x[k+0], S11,0xD76AA478); 
    d=FF(d,a,b,c,x[k+1], S12,0xE8C7B756); 
    c=FF(c,d,a,b,x[k+2], S13,0x242070DB); 
    b=FF(b,c,d,a,x[k+3], S14,0xC1BDCEEE); 
    a=FF(a,b,c,d,x[k+4], S11,0xF57C0FAF); 
    d=FF(d,a,b,c,x[k+5], S12,0x4787C62A); 
    c=FF(c,d,a,b,x[k+6], S13,0xA8304613); 
    b=FF(b,c,d,a,x[k+7], S14,0xFD469501); 
    a=FF(a,b,c,d,x[k+8], S11,0x698098D8); 
    d=FF(d,a,b,c,x[k+9], S12,0x8B44F7AF); 
    c=FF(c,d,a,b,x[k+10],S13,0xFFFF5BB1); 
    b=FF(b,c,d,a,x[k+11],S14,0x895CD7BE); 
    a=FF(a,b,c,d,x[k+12],S11,0x6B901122); 
    d=FF(d,a,b,c,x[k+13],S12,0xFD987193); 
    c=FF(c,d,a,b,x[k+14],S13,0xA679438E); 
    b=FF(b,c,d,a,x[k+15],S14,0x49B40821); 
    a=GG(a,b,c,d,x[k+1], S21,0xF61E2562); 
    d=GG(d,a,b,c,x[k+6], S22,0xC040B340); 
    c=GG(c,d,a,b,x[k+11],S23,0x265E5A51); 
    b=GG(b,c,d,a,x[k+0], S24,0xE9B6C7AA); 
    a=GG(a,b,c,d,x[k+5], S21,0xD62F105D); 
    d=GG(d,a,b,c,x[k+10],S22,0x2441453); 
    c=GG(c,d,a,b,x[k+15],S23,0xD8A1E681); 
    b=GG(b,c,d,a,x[k+4], S24,0xE7D3FBC8); 
    a=GG(a,b,c,d,x[k+9], S21,0x21E1CDE6); 
    d=GG(d,a,b,c,x[k+14],S22,0xC33707D6); 
    c=GG(c,d,a,b,x[k+3], S23,0xF4D50D87); 
    b=GG(b,c,d,a,x[k+8], S24,0x455A14ED); 
    a=GG(a,b,c,d,x[k+13],S21,0xA9E3E905); 
    d=GG(d,a,b,c,x[k+2], S22,0xFCEFA3F8); 
    c=GG(c,d,a,b,x[k+7], S23,0x676F02D9); 
    b=GG(b,c,d,a,x[k+12],S24,0x8D2A4C8A); 
    a=HH(a,b,c,d,x[k+5], S31,0xFFFA3942); 
    d=HH(d,a,b,c,x[k+8], S32,0x8771F681); 
    c=HH(c,d,a,b,x[k+11],S33,0x6D9D6122); 
    b=HH(b,c,d,a,x[k+14],S34,0xFDE5380C); 
    a=HH(a,b,c,d,x[k+1], S31,0xA4BEEA44); 
    d=HH(d,a,b,c,x[k+4], S32,0x4BDECFA9); 
    c=HH(c,d,a,b,x[k+7], S33,0xF6BB4B60); 
    b=HH(b,c,d,a,x[k+10],S34,0xBEBFBC70); 
    a=HH(a,b,c,d,x[k+13],S31,0x289B7EC6); 
    d=HH(d,a,b,c,x[k+0], S32,0xEAA127FA); 
    c=HH(c,d,a,b,x[k+3], S33,0xD4EF3085); 
    b=HH(b,c,d,a,x[k+6], S34,0x4881D05); 
    a=HH(a,b,c,d,x[k+9], S31,0xD9D4D039); 
    d=HH(d,a,b,c,x[k+12],S32,0xE6DB99E5); 
    c=HH(c,d,a,b,x[k+15],S33,0x1FA27CF8); 
    b=HH(b,c,d,a,x[k+2], S34,0xC4AC5665); 
    a=II(a,b,c,d,x[k+0], S41,0xF4292244); 
    d=II(d,a,b,c,x[k+7], S42,0x432AFF97); 
    c=II(c,d,a,b,x[k+14],S43,0xAB9423A7); 
    b=II(b,c,d,a,x[k+5], S44,0xFC93A039); 
    a=II(a,b,c,d,x[k+12],S41,0x655B59C3); 
    d=II(d,a,b,c,x[k+3], S42,0x8F0CCC92); 
    c=II(c,d,a,b,x[k+10],S43,0xFFEFF47D); 
    b=II(b,c,d,a,x[k+1], S44,0x85845DD1); 
    a=II(a,b,c,d,x[k+8], S41,0x6FA87E4F); 
    d=II(d,a,b,c,x[k+15],S42,0xFE2CE6E0); 
    c=II(c,d,a,b,x[k+6], S43,0xA3014314); 
    b=II(b,c,d,a,x[k+13],S44,0x4E0811A1); 
    a=II(a,b,c,d,x[k+4], S41,0xF7537E82); 
    d=II(d,a,b,c,x[k+11],S42,0xBD3AF235); 
    c=II(c,d,a,b,x[k+2], S43,0x2AD7D2BB); 
    b=II(b,c,d,a,x[k+9], S44,0xEB86D391); 
    a=AddUnsigned(a,AA); 
    b=AddUnsigned(b,BB); 
    c=AddUnsigned(c,CC); 
    d=AddUnsigned(d,DD); 
    } 

    var temp = WordToHex(a)+WordToHex(b)+WordToHex(c)+WordToHex(d); 

    return temp.toLowerCase(); 
    }-*/;
	
}
