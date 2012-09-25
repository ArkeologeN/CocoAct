package com.plumtree.cocoact;

import java.util.ArrayList;
import java.util.Random;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;

import android.view.MotionEvent;

public class GameLayer extends CCColorLayer {
	
	protected ArrayList<CCSprite> _targets;
	protected ArrayList<CCSprite> _projectiles;
	
	
	protected GameLayer(ccColor4B color) {
		// TODO Auto-generated constructor stub
		super(color);
		
		_targets = new ArrayList<CCSprite>();
		_projectiles = new ArrayList<CCSprite>();
		
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CCSprite _player = CCSprite.sprite("Player.png");
		
		_player.setPosition(CGPoint.ccp(_player.getContentSize().width / 2.0f, winSize.height / 2.0f));
		addChild(_player);
		
		this.setIsTouchEnabled(true);
		
		this.schedule("gameLogic", 1.0f);
		
	}
	
	public void gameLogic(float dt) {
		addTarget();
	}
	
	protected void addTarget() {
		
		Random rand = new Random();
		CCSprite target = CCSprite.sprite("Target.png");
		
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		int minY = (int)(target.getContentSize().height / 2.0f);
		int maxY = (int)(winSize.height - target.getContentSize().height / 2.0f);
		int rangeY = maxY - minY;
		int actualY = rand.nextInt(rangeY) + minY;
		
		target.setPosition(CGPoint.ccp(winSize.width + (target.getContentSize().width / 2.0f), actualY));
		addChild(target);
		
		target.setTag(1);
		_targets.add(target);
		
		int minDuration = 2;
		int maxDuration = 4;
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;
		
		CCMoveTo actionMove = CCMoveTo.action(actualDuration, CGPoint.ccp(
									-target.getContentSize().width / 2.0f, actualY));
		
		CCCallFunc actionMoveDone = CCCallFunc.action(this, "spriteMoveFinished");
		CCSequence actions = CCSequence.actions(actionMove
				, actionMoveDone);
		
		target.runAction(actions);
	}
	
	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCColorLayer layer = new GameLayer(ccColor4B.ccc4(255, 255, 255, 255));
		scene.addChild(layer);
		return scene;
	}
	
	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		// TODO Auto-generated method stub
		CGPoint location = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(event.getX(), event.getY()));
		
		// Change winSize to global Class variable
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CCSprite projectile = CCSprite.sprite("Projectile.png");
		
		projectile.setPosition(CGPoint.ccp(20, winSize.height / 2.0f));
		
		int offX = (int)(location.x - projectile.getPosition().x);
		int offY = (int)(location.y - projectile.getPosition().y);
		
		if (offX <= 0) {
			return true;
		}
		
		addChild(projectile);
		projectile.setTag(2);
		_projectiles.add(projectile);
		
		
		
		int realX = (int)(winSize.width + (projectile.getContentSize().width / 2.0f));
		float ratio = (float) offY / (float) offX;
		int realY = (int)((realX * ratio) + projectile.getPosition().x);
		CGPoint realDest = CGPoint.ccp(realX, realY);
		
		int offRealX = (int)(realX - projectile.getPosition().x);
		int offRealY = (int)(realY - projectile.getPosition().y);
		
		float length = (float)Math.sqrt((offRealX * offRealX) + (offRealY * offRealY));
		float velocity = 480.0f / 1.0f;
		float realMoveDuration = length / velocity;
		
		projectile.runAction(CCSequence.actions(
	            CCMoveTo.action(realMoveDuration, realDest),
	            CCCallFuncN.action(this, "spriteMoveFinished")));
		
		return true;
	}
	

	
	public void spriteMoveFinished(Object sender) {
		CCSprite sprite = (CCSprite) sender;
		if (sprite.getTag() == 1) 
			_targets.remove(sprite);
		else
			_projectiles.remove(sprite);
		this.removeChild(sprite, true);
		
	}
	
	public void update(float dt) {
		ArrayList<CCSprite> projectilesToDelete = new ArrayList<CCSprite>();
		
		for (CCSprite projectile : _projectiles) {			
			CGRect projectileRect =  CGRect.make(projectile.getPosition().x - (projectile.getContentSize().width / 2.0f),
					                    projectile.getPosition().y - (projectile.getContentSize().height / 2.0f),
					                    projectile.getContentSize().width,
					                    projectile.getContentSize().height);
			
			ArrayList<CCSprite> targetsToDelete = new ArrayList<CCSprite>();
			
			for (CCSprite target : _targets) {				
				CGRect targetRect = CGRect.make(target.getPosition().x - (target.getContentSize().width),
                        target.getPosition().y - (target.getContentSize().height),
                        target.getContentSize().width,
                        target.getContentSize().height);
				
				if (CGRect.intersects(projectileRect, targetRect)) {
					
				}
				
			}
		}
	}

}
