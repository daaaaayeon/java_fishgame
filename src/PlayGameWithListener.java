//2019학년도 2학기 프로젝트1 (Java Project)
//의료IT공학과 20185110 성다연

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

	int NEW_FISH_INTERVAL =5;			//새로운 물고기의 생성을 계산하기 위한 변수
	int DELAY =50;  					//물고기의 움직임을 위한 딜레이 변수
	int WIDTH = 1000;
	int HEIGHT = 800;
	int userLevel=1;					//레벨 1부터 시작하기 위한 변수
	int userX, userY;      				//사용자의 좌표값
	int userSpeed=20;					//레벨1 사용자 속도 지정
	int score=0;
	int time =0;						//사용자의 게임 시간
	int finishTime = 60;				//종료하기 위해 줄어드는 게임 시간
	int twoDirection;					//방향을 위한 랜덤 변수
	int thisDirection;					//방향 지정(왼쪽 또는 오른쪽)
	int downFrame =0;					//배경이 내려가는 효과를 주기 위한 변수
	int finalScore;						//최종 점수
	int count=0;						//키보드리스너를 활용하기 위한 변수
	
	String myScore, remainingTime;		//결과값을 문자열로 출력하기 위한 변수

	//키보드 리스너 제어하기 위한 논리자료형 변수들
	boolean keyLeft=false;
	boolean keyRight=false;
	boolean fishShow=false;
	boolean getScore=false;
	boolean moveLeft=false;
	boolean moveRight=false;
	boolean moveUp=false;
	boolean moveDown=false;

	//아이템 출력을 위한 논리자료형 변수들
	boolean item=false;
	boolean dolphinOK=false;
	boolean pearlOK=false;
	boolean plasticOK=false;

	//배경을 위한 이미지
	Image background =new ImageIcon("src/Image/backImage.png").getImage();
	Image startImage =new ImageIcon("src/Image/startImage1.png").getImage();
	Image resultImage =new ImageIcon("src/Image/resultImage.png").getImage();
	Image explainImage =new ImageIcon("src/Image/explainImage2.png").getImage();

	//아이템을 위한 이미지
	Image dolphin =new ImageIcon("src/Image/dolphin.png").getImage();
	Image pearl =new ImageIcon("src/Image/pearl.png").getImage();
	Image plastic =new ImageIcon("src/Image/plastic.png").getImage();

	//아이템  좌표값
	int dolphinX; int dolphinY;
	int pearlX; int pearlY;
	int plasticX; int plasticY;
	
	//시간과 움직임을 위한 타이머
	Timer goClock;
	Timer goAnime;
	
	ShapeFish userFish;											//사용자 물고기 객체선언
	ArrayList<ShapeFish> attackFishList = new ArrayList<>();	//공격자 물고기 배열객체 선언
	
	//음악실행
	private final String START_SOUND = "/Music/UndertheSea3.wav";
	private AudioClip backgroundSound; // 게임 배경 음악
	
	//메인 메소드
	public static void main(String[] args) {
		PlayGameWithListener finalGame = new PlayGameWithListener();
		finalGame.play();
	}
	
	//프레임 창 메소드
	public void play() {
		frame = new JFrame ();
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

		userFish = new ShapeFish("src/Image/fish1-1.png", 500, 400, 1, userSpeed);
		
		controlPanel = new JPanel();
		scoreLabel = new JLabel("내 점수 = "+score+", ");
		levelLabel = new JLabel("내 레벨 = "+userLevel);

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
			System.out.println("음향 파일 로딩 실패");
		}
		
		backgroundSound.play();
		
		frame.addMouseListener(new Click());
		frame.addKeyListener(new MoveOfUserListener());
		frame.setPreferredSize (new Dimension(WIDTH, HEIGHT));
		frame.pack();
		frame.setVisible(true);
	}

	//레벨과 방향에 따라 물고지 이미지 변경해주는 메소드
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

	//초기 공격자 물고기 메소드
	public void prepareFish() {
		for(int i=0; i<10; i++) {
			int rand =(int)(Math.random()*3)+1;
			attackFishImage(rand);
		}
	}
	
	//시작 패널
	public class StartPanel extends JPanel{
		public void paintComponent(Graphics g) {
			g.drawImage(startImage,0,0,this);
			
		}
	}
	
	//설명 패널
	public class ExplainPanel extends JPanel{
		public void paintComponent(Graphics g) {
			g.drawImage(explainImage,0,0,this);
			
		}
	}
	
	//결과 패널
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

	//게임 패널
	class DrawFishPanel extends JPanel{
		public void paintComponent (Graphics g) {
			count=1;	//키보드 리스터 활용하기 위한 변수(게임 시작되면 설명을 못 보게 설정함)

			g.drawImage(background,0,downFrame,this);

			userFish.drawUserFish(g);

			for(ShapeFish attackFish : attackFishList) {
				attackFish.drawAttackFish(g);
				attackFish.move();
			}

			//레벨에 따라 출력되는 아이템 다르게 설정하기
			if(userLevel>0)
				if(item) 
					g.drawImage(dolphin,dolphinX, dolphinY,60,60, this);

			if(userLevel>=3)
				if(item) 
					g.drawImage(pearl,pearlX, pearlY,60,60, this);

			if(userLevel==5)
				if(item) 
					g.drawImage(plastic,plasticX, plasticY,60,60, this);

			//화면 아래쪽에 줄어드는 시간 출력하기
			g.setFont(new Font("Times",  Font.BOLD | Font.ITALIC, 35));
			g.setColor(Color.red);

			try {
				g.drawString(remainingTime, 900, 700);
			} catch(Exception e) {
				System.out.println("바다 속으로 내려가는 중~");
			}
			
		}
	}

	//물고기 닿았을때 score구현 
	public void attackedFish() {
		for(ShapeFish s : attackFishList) {
			if(s.collide(userFish)) {
				if(s.getLevel() <= userLevel) { 
					score++;
					System.out.println("작은 물고기와 충돌>>>점수획득");
				}
				else {
					score--;
					System.out.println("큰 물고기와 충돌>>>점수상실");
				}
				//부딛힌 물고기는 바로 지워주기 
				attackFishList.remove(s);
				break;
			}
		}
	}
	
	//게임이 종료되었을 때 실행되는 메소드
	public void finishGame() {
		goClock.stop();
		goAnime.stop();
		for(ShapeFish attackFish : attackFishList)
			attackFish.stop();
		backgroundSound.stop();
		cardLayout.show(cards, "4");
	}
	
	//아이템 효과를 위한 메소드
	public void itemEffect() {
		Point dolphinP = new Point(dolphinX, dolphinY);
		if(dolphinP.distance(userFish.point)<=50) {
			dolphinOK=true;
			System.out.println("돌고래>>>시간 증가");
		}
		else
			dolphinOK=false;

		if(userLevel>=3) {
			Point pearlP = new Point(pearlX, pearlY);
			if(pearlP.distance(userFish.point)<=50) {
				pearlOK=true;
				System.out.println("진주>>>score가 증가함");
			}
			else
				pearlOK=false;
		}

		if(userLevel==5) {
			Point plasticP = new Point(plasticX, plasticY);
			if(plasticP.distance(userFish.point)<=100) {
				plasticOK=true;
				System.out.println("쓰레기>>>게임 종료");
			}
			else
				plasticOK=false;
		}
	}


	//공격자물고기의 랜덤변수 지정, 점수에 따른 레벨 설정, 아이템 구현을 위한 메소드
	class AnimeListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			downFrame-=1;
			if(downFrame==-800) downFrame=0;
			
			twoDirection = (int)(Math.random()*10);

			if(twoDirection>5)
				thisDirection=1;
			else
				thisDirection=-1;

			fishMove(); //물고기 움직임 

			if(score > 5 && score <=15)
				userLevel=2;
			if(score > 15 && score <=30)
				userLevel=3;
			if(score > 30 && score <=45)
				userLevel=4;
			if(score > 50)
				userLevel=5;

			//만약 움직이다 화면밖으로나갔으면 리스트에서 제거
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
			levelLabel.setText("내 레벨 = "+userLevel);	
			scoreLabel.setText("내 점수 = "+score+", ");
			
			if(userLevel==5) {
				if(plasticOK) {
					finishGame();
				}
			}
		}	
	}


	//물고기, 아이템 생성 및 시간의 흐름 조절하는 메소드
	class ClockListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			time++;
			
			prepareFish();

			//일정 시간지날때마다 물고기 생성 
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

			//아이템 좌표값을 랜덤으로 지정
			dolphinX = (int)(Math.random()*(WIDTH-60));
			dolphinY = (int)(Math.random()*(HEIGHT-60));
			pearlX = (int)(Math.random()*(WIDTH-60));
			pearlY = (int)(Math.random()*(HEIGHT-60));
			plasticX = (int)(Math.random()*(WIDTH-60));
			plasticY = (int)(Math.random()*(HEIGHT-60));

			//지정된 시간마다 아이템 출력
			if(time%3 == 0)
				item=true;
			else
				item=false;


			if(finishTime==0) {
				System.out.println("주어진 시간 종료");
				finishGame();
			}

			if(dolphinOK) {
				finishTime=finishTime+10;
				frame.repaint();
			}
			else {
				finishTime--;
			}
			
			//결과값 출력을 위한 문자열 처리
			remainingTime=""+finishTime;
			finalScore=time*score/userLevel;
			myScore=""+finalScore;
		}
	}

	//레벨에 따른 공격자 물고기 이미지 설정, 속도를 따로 지정해줌.
	//thisDirection=양수 >> 오른쪽에서 향하기 때문에 왼쪽 프레임 밖에서 x좌표 지정
	//thisDirection=음수 >> 왼쪽에서 향하기 때문에 오른쪽 프레임 밖에서 x좌표 지정
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

	//사용자 물고기 움직임 메소드
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
	
	//키보드 리스너를 이용해 사용자 물고기 움직이는 효과 구현하기
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
			
			//엔터키를 통해 패널 변경(숨겨진 개발자 치트키1)
			if(key==KeyEvent.VK_ENTER) {

				count++;

				if(count==1) {
					cardLayout.show(cards, "2");	//엔터:게임설명
				}
				if(count==2)
					finishGame();					//두번엔터:게임종료
				if(count==3)
					System.exit(0); 				//세번엔터:프레임끄기
				
			}
			
			//스페이스키를 통해 패널 변경(숨겨진 개발자 치트키2)
			if(key==KeyEvent.VK_SPACE) {
				userLevel++;
				if(userLevel==5) score=55;
			}
		}
		
		//키보드를 뗐을 때 논리자료형 false로 지정 >>> 원활한 움직임을 위해 구현
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

	//게임시작을 위한 마우스리스너 메소드
	class Click implements MouseListener{
		public void mouseClicked(MouseEvent e) {
			cardLayout.show(cards, "3"); //클릭:게임시작
			goAnime.start();
			goClock.start();
			
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
}
