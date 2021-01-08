import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;

//물고기들의 전체적인 움직임 클래스
public class ShapeFish extends ImageIcon {
						
	int level;						//더 낮은 레벨의 물고기만 잡아 먹을 수 있도록 설정
	int speedWithDirection;			//level에 따라 속도 증가
	int speed;						//물고기의 보폭(속도 제어)
	String img;
	Point point;					//공격물고기들의 좌표를 담은 Point
	ImageIcon leftFish;				//왼쪽으로 향해서 이동하는 물고기
	ImageIcon rightFish;			//오른쪽으로 향해서 이동하는 물고기
	int WIDTH =1800;

	public ShapeFish(int level,int speed) {
		this.level= level;
		this.speed=speed;
	}
	
	public ShapeFish(int x, int y, int level, int speed) {
		point =new Point(x,y);
		this.level=level;
		speedWithDirection=speed;
		//level에 따라 이미지 파일명 설정 (문자열 처리)
		leftFish = new ImageIcon("src/Image/fish"+ level+"-1.png");
		rightFish = new ImageIcon("src/Image/fish"+ level+"-2.png");
	}

	public ShapeFish(String img, int x, int y, int level, int speed) {
		super(img);
		point =new Point(x,y); 
		this.level=level; 
		this.speedWithDirection=speed;
	}

	//물고기 움직이는 모습 표현
	public void move() {
		point.x += speedWithDirection;
	}

	public void stop() {
		speedWithDirection=0;
	}
	//물고기를 지정된 위치로 움직이는 메소드(사용자 물고기 해당)
	public void setLocation(int x, int y) {
		point.x=x; point.y=y;
	}

	//speedWithDirection가 음수 >>> 물고기의 x좌표가 감소하기 때문에 왼쪽으로 이동
	//speedWithDirection가 양수 >>> 물고기의 x좌표가 증가하기 때문에 오른쪽으로 이동
	public void drawAttackFish(Graphics g) {
		if(speedWithDirection <0)
			g.drawImage(leftFish.getImage(), point.x, point.y, null);
		else
			g.drawImage(rightFish.getImage(), point.x, point.y, null);
	}

	public void drawUserFish(Graphics g) {
		g.drawImage(this.getImage(), point.x, point.y, null);
	}

	//필요한 setter,getter 
	public int getX() {
		return point.x;
	}
	public int getY() {
		return point.y;
	}
	public int getLevel() {
		return level;
	}
	public void setImage(String image) {
		this.img=image;
	}
	
	//충돌했을 경우의 거리 측정 메소드
	public boolean collide(ShapeFish s) {
		Point p1=new Point(s.getX(),s.getY());
		if(point.distance(p1)<=50) return true;
		
		return false;
	}
	
	//프레임 밖을 벗어났을 경우을 위한 메소드
	public boolean goOutside() {
		if(this.getX()<=-100 || this.getX() >= WIDTH)
			return true;
		return false;
	}
}

