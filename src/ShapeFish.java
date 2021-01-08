import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;

//�������� ��ü���� ������ Ŭ����
public class ShapeFish extends ImageIcon {
						
	int level;						//�� ���� ������ ����⸸ ��� ���� �� �ֵ��� ����
	int speedWithDirection;			//level�� ���� �ӵ� ����
	int speed;						//������� ����(�ӵ� ����)
	String img;
	Point point;					//���ݹ������� ��ǥ�� ���� Point
	ImageIcon leftFish;				//�������� ���ؼ� �̵��ϴ� �����
	ImageIcon rightFish;			//���������� ���ؼ� �̵��ϴ� �����
	int WIDTH =1800;

	public ShapeFish(int level,int speed) {
		this.level= level;
		this.speed=speed;
	}
	
	public ShapeFish(int x, int y, int level, int speed) {
		point =new Point(x,y);
		this.level=level;
		speedWithDirection=speed;
		//level�� ���� �̹��� ���ϸ� ���� (���ڿ� ó��)
		leftFish = new ImageIcon("src/Image/fish"+ level+"-1.png");
		rightFish = new ImageIcon("src/Image/fish"+ level+"-2.png");
	}

	public ShapeFish(String img, int x, int y, int level, int speed) {
		super(img);
		point =new Point(x,y); 
		this.level=level; 
		this.speedWithDirection=speed;
	}

	//����� �����̴� ��� ǥ��
	public void move() {
		point.x += speedWithDirection;
	}

	public void stop() {
		speedWithDirection=0;
	}
	//����⸦ ������ ��ġ�� �����̴� �޼ҵ�(����� ����� �ش�)
	public void setLocation(int x, int y) {
		point.x=x; point.y=y;
	}

	//speedWithDirection�� ���� >>> ������� x��ǥ�� �����ϱ� ������ �������� �̵�
	//speedWithDirection�� ��� >>> ������� x��ǥ�� �����ϱ� ������ ���������� �̵�
	public void drawAttackFish(Graphics g) {
		if(speedWithDirection <0)
			g.drawImage(leftFish.getImage(), point.x, point.y, null);
		else
			g.drawImage(rightFish.getImage(), point.x, point.y, null);
	}

	public void drawUserFish(Graphics g) {
		g.drawImage(this.getImage(), point.x, point.y, null);
	}

	//�ʿ��� setter,getter 
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
	
	//�浹���� ����� �Ÿ� ���� �޼ҵ�
	public boolean collide(ShapeFish s) {
		Point p1=new Point(s.getX(),s.getY());
		if(point.distance(p1)<=50) return true;
		
		return false;
	}
	
	//������ ���� ����� ����� ���� �޼ҵ�
	public boolean goOutside() {
		if(this.getX()<=-100 || this.getX() >= WIDTH)
			return true;
		return false;
	}
}

