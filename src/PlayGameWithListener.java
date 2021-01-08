//2019�г⵵ 2�б� ������Ʈ1 (Java Project)
//�Ƿ�IT���а� 20185110 ���ٿ�

import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PlayGameWithListener {

	JFrame frame;
	JLabel scoreLabel, levelLabel, timeLabel;
	JPanel controlPanel, startPanel, cards, resultPanel, explainPanel;
	DrawFishPanel attackFishPanel;
	CardLayout cardLayout;

	int NEW_FISH_INTERVAL =5;			//���ο� ������� ������ ����ϱ� ���� ����
	int DELAY =50;  					//������� �������� ���� ������ ����
	int WIDTH = 1000;
	int HEIGHT = 800;
	int userLevel=1;					//���� 1���� �����ϱ� ���� ����
	int userX, userY;      				//������� ��ǥ��
	int userSpeed=20;					//����1 ����� �ӵ� ����
	int score=0;
	int time =0;						//������� ���� �ð�
	int finishTime = 60;				//�����ϱ� ���� �پ��� ���� �ð�
	int twoDirection;					//������ ���� ���� ����
	int thisDirection;					//���� ����(���� �Ǵ� ������)
	int downFrame =0;					//����� �������� ȿ���� �ֱ� ���� ����
	int finalScore;						//���� ����
	int count=0;						//Ű���帮���ʸ� Ȱ���ϱ� ���� ����
	
	String myScore, remainingTime;		//������� ���ڿ��� ����ϱ� ���� ����

	//Ű���� ������ �����ϱ� ���� ���ڷ��� ������
	boolean keyLeft=false;
	boolean keyRight=false;
	boolean fishShow=false;
	boolean getScore=false;
	boolean moveLeft=false;
	boolean moveRight=false;
	boolean moveUp=false;
	boolean moveDown=false;

	//������ ����� ���� ���ڷ��� ������
	boolean item=false;
	boolean dolphinOK=false;
	boolean pearlOK=false;
	boolean plasticOK=false;

	//����� ���� �̹���
	Image background =new ImageIcon("src/Image/backImage.png").getImage();
	Image startImage =new ImageIcon("src/Image/startImage1.png").getImage();
	Image resultImage =new ImageIcon("src/Image/resultImage.png").getImage();
	Image explainImage =new ImageIcon("src/Image/explainImage2.png").getImage();

	//�������� ���� �̹���
	Image dolphin =new ImageIcon("src/Image/dolphin.png").getImage();
	Image pearl =new ImageIcon("src/Image/pearl.png").getImage();
	Image plastic =new ImageIcon("src/Image/plastic.png").getImage();

	//������  ��ǥ��
	int dolphinX; int dolphinY;
	int pearlX; int pearlY;
	int plasticX; int plasticY;
	
	//�ð��� �������� ���� Ÿ�̸�
	Timer goClock;
	Timer goAnime;
	
	ShapeFish userFish;											//����� ����� ��ü����
	ArrayList<ShapeFish> attackFishList = new ArrayList<>();	//������ ����� �迭��ü ����
	
	//���ǽ���
	private final String START_SOUND = "/Music/UndertheSea3.wav";
	private AudioClip backgroundSound; // ���� ��� ����
	
	//���� �޼ҵ�
	public static void main(String[] args) {
		PlayGameWithListener finalGame = new PlayGameWithListener();
		finalGame.play();
	}
	
	//������ â �޼ҵ�
	public void play() {
		frame = new JFrame ();
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

		userFish = new ShapeFish("src/Image/fish1-1.png", 500, 400, 1, userSpeed);
		
		controlPanel = new JPanel();
		scoreLabel = new JLabel("�� ���� = "+score+", ");
		levelLabel = new JLabel("�� ���� = "+userLevel);

		controlPanel.add(scoreLabel);
		controlPanel.add(levelLabel);

		startPanel = new StartPanel();
		attackFishPanel = new DrawFishPanel();
		resultPanel = new ResultPanel();
		explainPanel = new ExplainPanel();

		cards = new JPanel(new CardLayout());

		cards.add(startPanel, "1");
		cards.add(explainPanel, "2");
		cards.add(attackFishPanel, "3");
		cards.add(resultPanel, "4");

		cardLayout = (CardLayout) cards.getLayout();

		frame.add(BorderLayout.NORTH, controlPanel);
		frame.getContentPane().add(cards);

		goAnime = new Timer(DELAY, new AnimeListener());
		goClock =new Timer (1000,new ClockListener());
	
		try {
			backgroundSound = JApplet.newAudioClip(getClass().getResource(START_SOUND));
		} catch (Exception e) {
			System.out.println("���� ���� �ε� ����");
		}
		
		backgroundSound.play();
		
		frame.addMouseListener(new Click());
		frame.addKeyListener(new MoveOfUserListener());
		frame.setPreferredSize (new Dimension(WIDTH, HEIGHT));
		frame.pack();
		frame.setVisible(true);
	}

	//������ ���⿡ ���� ������ �̹��� �������ִ� �޼ҵ�
	public void userFishImage(int level) {
		switch(level) {
		case 1:
			if(moveLeft)
				userFish.setImage(new ImageIcon("src/Image/fish1-1.png").getImage());
			if(moveRight)
				userFish.setImage(new ImageIcon("src/Image/fish1-2.png").getImage());
			break;
		case 2:
			if(moveLeft)
				userFish.setImage(new ImageIcon("src/Image/fish2-1.png").getImage());
			if(moveRight)
				userFish.setImage(new ImageIcon("src/Image/fish2-2.png").getImage());
			break;
		case 3:
			if(moveLeft)
				userFish.setImage(new ImageIcon("src/Image/fish3-1.png").getImage());
			if(moveRight)
				userFish.setImage(new ImageIcon("src/Image/fish3-2.png").getImage());
			break;
		case 4:
			if(moveLeft)
				userFish.setImage(new ImageIcon("src/Image/fish4-1.png").getImage());
			if(moveRight)
				userFish.setImage(new ImageIcon("src/Image/fish4-2.png").getImage());
			break;
		case 5:
			if(moveLeft)
				userFish.setImage(new ImageIcon("src/Image/fish5-1.png").getImage());
			if(moveRight)
				userFish.setImage(new ImageIcon("src/Image/fish5-2.png").getImage());
			break;
		}
	}

	//�ʱ� ������ ����� �޼ҵ�
	public void prepareFish() {
		for(int i=0; i<10; i++) {
			int rand =(int)(Math.random()*3)+1;
			attackFishImage(rand);
		}
	}
	
	//���� �г�
	public class StartPanel extends JPanel{
		public void paintComponent(Graphics g) {
			g.drawImage(startImage,0,0,this);
			
		}
	}
	
	//���� �г�
	public class ExplainPanel extends JPanel{
		public void paintComponent(Graphics g) {
			g.drawImage(explainImage,0,0,this);
			
		}
	}
	
	//��� �г�
	public class ResultPanel extends JPanel{
		public void paintComponent(Graphics g) {
			g.drawImage(resultImage,0,0,this);

			g.setFont(new Font("Gothic", Font.BOLD, 50));
			g.drawString("Finish!", 450, 325);

			g.setFont(new Font("Times",  Font.BOLD | Font.ITALIC, 35));
			g.drawString("My Score is "+myScore+".", 400, 400);
			System.out.println(myScore);
		}
	}

	//���� �г�
	class DrawFishPanel extends JPanel{
		public void paintComponent (Graphics g) {
			count=1;	//Ű���� ������ Ȱ���ϱ� ���� ����(���� ���۵Ǹ� ������ �� ���� ������)

			g.drawImage(background,0,downFrame,this);

			userFish.drawUserFish(g);

			for(ShapeFish attackFish : attackFishList) {
				attackFish.drawAttackFish(g);
				attackFish.move();
			}

			//������ ���� ��µǴ� ������ �ٸ��� �����ϱ�
			if(userLevel>0)
				if(item) 
					g.drawImage(dolphin,dolphinX, dolphinY,60,60, this);

			if(userLevel>=3)
				if(item) 
					g.drawImage(pearl,pearlX, pearlY,60,60, this);

			if(userLevel==5)
				if(item) 
					g.drawImage(plastic,plasticX, plasticY,60,60, this);

			//ȭ�� �Ʒ��ʿ� �پ��� �ð� ����ϱ�
			g.setFont(new Font("Times",  Font.BOLD | Font.ITALIC, 35));
			g.setColor(Color.red);

			try {
				g.drawString(remainingTime, 900, 700);
			} catch(Exception e) {
				System.out.println("�ٴ� ������ �������� ��~");
			}
			
		}
	}

	//����� ������� score���� 
	public void attackedFish() {
		for(ShapeFish s : attackFishList) {
			if(s.collide(userFish)) {
				if(s.getLevel() <= userLevel) { 
					score++;
					System.out.println("���� ������ �浹>>>����ȹ��");
				}
				else {
					score--;
					System.out.println("ū ������ �浹>>>�������");
				}
				//�ε��� ������ �ٷ� �����ֱ� 
				attackFishList.remove(s);
				break;
			}
		}
	}
	
	//������ ����Ǿ��� �� ����Ǵ� �޼ҵ�
	public void finishGame() {
		goClock.stop();
		goAnime.stop();
		for(ShapeFish attackFish : attackFishList)
			attackFish.stop();
		backgroundSound.stop();
		cardLayout.show(cards, "4");
	}
	
	//������ ȿ���� ���� �޼ҵ�
	public void itemEffect() {
		Point dolphinP = new Point(dolphinX, dolphinY);
		if(dolphinP.distance(userFish.point)<=50) {
			dolphinOK=true;
			System.out.println("����>>>�ð� ����");
		}
		else
			dolphinOK=false;

		if(userLevel>=3) {
			Point pearlP = new Point(pearlX, pearlY);
			if(pearlP.distance(userFish.point)<=50) {
				pearlOK=true;
				System.out.println("����>>>score�� ������");
			}
			else
				pearlOK=false;
		}

		if(userLevel==5) {
			Point plasticP = new Point(plasticX, plasticY);
			if(plasticP.distance(userFish.point)<=100) {
				plasticOK=true;
				System.out.println("������>>>���� ����");
			}
			else
				plasticOK=false;
		}
	}


	//�����ڹ������ �������� ����, ������ ���� ���� ����, ������ ������ ���� �޼ҵ�
	class AnimeListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			downFrame-=1;
			if(downFrame==-800) downFrame=0;
			
			twoDirection = (int)(Math.random()*10);

			if(twoDirection>5)
				thisDirection=1;
			else
				thisDirection=-1;

			fishMove(); //����� ������ 

			if(score > 5 && score <=15)
				userLevel=2;
			if(score > 15 && score <=30)
				userLevel=3;
			if(score > 30 && score <=45)
				userLevel=4;
			if(score > 50)
				userLevel=5;

			//���� �����̴� ȭ������γ������� ����Ʈ���� ����
			for(ShapeFish s : attackFishList) {
				if(s.goOutside()){
					attackFishList.remove(s);
					break;
				}
			}

			itemEffect();

			if(userLevel>=3) {
				if(pearlOK)
					score=score+2;
			}

			userFishImage(userLevel);
			attackedFish();
			frame.repaint();
			levelLabel.setText("�� ���� = "+userLevel);	
			scoreLabel.setText("�� ���� = "+score+", ");
			
			if(userLevel==5) {
				if(plasticOK) {
					finishGame();
				}
			}
		}	
	}


	//�����, ������ ���� �� �ð��� �帧 �����ϴ� �޼ҵ�
	class ClockListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			time++;
			
			prepareFish();

			//���� �ð����������� ����� ���� 
			if(time% NEW_FISH_INTERVAL == 0) {
				if(userLevel<3) {
					int rand =(int)(Math.random()*3)+1; //1,2,3
					attackFishImage(rand);
				}
				else if(userLevel<4) {
					int rand =(int)(Math.random()*3)+2; //2,3,4
					attackFishImage(rand);
				}
				else if(userLevel<=5) {
					int rand =(int)(Math.random()*4)+2; //2,3,4,5
					attackFishImage(rand);
				}
			}

			//������ ��ǥ���� �������� ����
			dolphinX = (int)(Math.random()*(WIDTH-60));
			dolphinY = (int)(Math.random()*(HEIGHT-60));
			pearlX = (int)(Math.random()*(WIDTH-60));
			pearlY = (int)(Math.random()*(HEIGHT-60));
			plasticX = (int)(Math.random()*(WIDTH-60));
			plasticY = (int)(Math.random()*(HEIGHT-60));

			//������ �ð����� ������ ���
			if(time%3 == 0)
				item=true;
			else
				item=false;


			if(finishTime==0) {
				System.out.println("�־��� �ð� ����");
				finishGame();
			}

			if(dolphinOK) {
				finishTime=finishTime+10;
				frame.repaint();
			}
			else {
				finishTime--;
			}
			
			//����� ����� ���� ���ڿ� ó��
			remainingTime=""+finishTime;
			finalScore=time*score/userLevel;
			myScore=""+finalScore;
		}
	}

	//������ ���� ������ ����� �̹��� ����, �ӵ��� ���� ��������.
	//thisDirection=��� >> �����ʿ��� ���ϱ� ������ ���� ������ �ۿ��� x��ǥ ����
	//thisDirection=���� >> ���ʿ��� ���ϱ� ������ ������ ������ �ۿ��� x��ǥ ����
	public void attackFishImage(int level) {
		switch(level){

		case 1:
			if(thisDirection==1) {
				attackFishList.add(new ShapeFish((int)(Math.random()*(WIDTH-50))-WIDTH,
						(int)(Math.random()*(HEIGHT-60)), 1, thisDirection*28));
			}
			else if(thisDirection==-1) {
				attackFishList.add(new ShapeFish((int)(Math.random()*(WIDTH-50))+WIDTH,
						(int)(Math.random()*(HEIGHT-60)), 1, thisDirection*28));
			}
			break;

		case 2:
			if(thisDirection==1) {
				attackFishList.add( new ShapeFish((int)(Math.random()*(WIDTH-50))-WIDTH,
						(int)(Math.random()*(HEIGHT-60)), 2, thisDirection*25));
			}
			else if(thisDirection==-1) {
				attackFishList.add( new ShapeFish((int)(Math.random()*(WIDTH-50))+WIDTH,
						(int)(Math.random()*(HEIGHT-60)), 2, thisDirection*25));
			}
			break;

		case 3:

			if(thisDirection==1) {
				attackFishList.add( new ShapeFish((int)(Math.random()*(WIDTH-50))-WIDTH,
						(int)(Math.random()*(HEIGHT-60)), 3, thisDirection*22));
			}
			else if(thisDirection==-1) {
				attackFishList.add( new ShapeFish((int)(Math.random()*(WIDTH-50))+WIDTH,
						(int)(Math.random()*(HEIGHT-60)), 3, thisDirection*22));
			}
			break;

		case 4:
			if(thisDirection==1) {
				attackFishList.add(new ShapeFish((int)(Math.random()*(WIDTH-50))-WIDTH,
						(int)(Math.random()*(HEIGHT-60)), 4, thisDirection*19));
			}
			else if(thisDirection==-1) {
				attackFishList.add(new ShapeFish((int)(Math.random()*(WIDTH-50))+WIDTH,
						(int)(Math.random()*(HEIGHT-60)), 4, thisDirection*19));
			}
			break;

		case 5:
			if(thisDirection==1) {
				attackFishList.add(new ShapeFish((int)(Math.random()*(WIDTH-50))-WIDTH,
						(int)(Math.random()*(HEIGHT-60)), 4, thisDirection*19));
			}
			else if(thisDirection==-1) {
				attackFishList.add(new ShapeFish((int)(Math.random()*(WIDTH-50))+WIDTH,
						(int)(Math.random()*(HEIGHT-60)), 4, thisDirection*19));
			}
			break;
			
		case 6:
			if(thisDirection==1) {
				attackFishList.add (new ShapeFish((int)(Math.random()*(WIDTH-50))-WIDTH,
						(int)(Math.random()*(HEIGHT-60)), 5, thisDirection*16));
			}
			else if(thisDirection==-1) {
				attackFishList.add(new ShapeFish((int)(Math.random()*(WIDTH-50))+WIDTH,
						(int)(Math.random()*(HEIGHT-60)), 5, thisDirection*16));
			}
			break;
		}
	}

	//����� ����� ������ �޼ҵ�
	public void fishMove() {
		if(moveUp)
			if(userFish.getY()>=0)
				userFish.setLocation(userFish.getX(), userFish.getY()-userSpeed);
		if(moveDown)
			if(userFish.getY()<=HEIGHT-50)
				userFish.setLocation(userFish.getX(), userFish.getY()+userSpeed);
		if(moveLeft)
			if(userFish.getX()>=0)
				userFish.setLocation(userFish.getX()-userSpeed, userFish.getY());
		if(moveRight)
			if(userFish.getX()<=WIDTH-50)
				userFish.setLocation(userFish.getX()+userSpeed, userFish.getY());
	}
	
	//Ű���� �����ʸ� �̿��� ����� ����� �����̴� ȿ�� �����ϱ�
	public class MoveOfUserListener implements KeyListener{
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if(key==KeyEvent.VK_UP)
				moveUp=true;
			if(key==KeyEvent.VK_DOWN)
				moveDown=true;
			if(key==KeyEvent.VK_LEFT) {
				keyLeft = true;
				keyRight = false;
				moveLeft=true;
			}
			if(key==KeyEvent.VK_RIGHT) {
				keyLeft = false;
				keyRight = true;
				moveRight=true;
			}
			
			//����Ű�� ���� �г� ����(������ ������ ġƮŰ1)
			if(key==KeyEvent.VK_ENTER) {

				count++;

				if(count==1) {
					cardLayout.show(cards, "2");	//����:���Ӽ���
				}
				if(count==2)
					finishGame();					//�ι�����:��������
				if(count==3)
					System.exit(0); 				//��������:�����Ӳ���
				
			}
			
			//�����̽�Ű�� ���� �г� ����(������ ������ ġƮŰ2)
			if(key==KeyEvent.VK_SPACE) {
				userLevel++;
				if(userLevel==5) score=55;
			}
		}
		
		//Ű���带 ���� �� ���ڷ��� false�� ���� >>> ��Ȱ�� �������� ���� ����
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			if(key==KeyEvent.VK_UP)
				moveUp=false;
			if(key==KeyEvent.VK_DOWN)
				moveDown=false;
			if(key==KeyEvent.VK_LEFT)
				moveLeft=false;
			if(key==KeyEvent.VK_RIGHT)
				moveRight=false;
		}
		public void keyTyped(KeyEvent arg0) {}
	}

	//���ӽ����� ���� ���콺������ �޼ҵ�
	class Click implements MouseListener{
		public void mouseClicked(MouseEvent e) {
			cardLayout.show(cards, "3"); //Ŭ��:���ӽ���
			goAnime.start();
			goClock.start();
			
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
}
