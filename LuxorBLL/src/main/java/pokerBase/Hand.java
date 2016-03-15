package pokerBase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;

import exceptions.DeckException;
import exceptions.HandException;
import pokerEnums.*;

import static java.lang.System.out;
import static java.lang.System.err;

public class Hand {

	private ArrayList<Card> CardsInHand;
	private ArrayList<Card> BestCardsInHand;
	private HandScore HandScore;
	private boolean bScored = false;

	public Hand() {
		CardsInHand = new ArrayList<Card>();
		BestCardsInHand = new ArrayList<Card>();
		HandScore = new HandScore();
	}
	
	private static int numberOfJokers(Hand h) {
		int numJokers = 0;
		for(Card c: h.getCardsInHand()){
			if(c.geteRank() == eRank.JOKER){
				numJokers += 1;
			}
		}
		return numJokers;
	}
	
	private static ArrayList<Hand> createPossibleHands(Hand h) {
		Deck d = new Deck();
		ArrayList<Hand> possibleHands = new ArrayList<Hand>();
		Collections.sort(h.getCardsInHand(), Card.CardRank);
		for(Card c : d.getCardsInDeck()){
			Hand newH = new Hand();
			ArrayList<Card> newHCards = new ArrayList<Card>();
			newHCards.add(c);
			newHCards.add(h.getCardsInHand().get(1));
			newHCards.add(h.getCardsInHand().get(2));
			newHCards.add(h.getCardsInHand().get(3));
			newHCards.add(h.getCardsInHand().get(4));
			newH.setCardsInHand(newHCards);
			possibleHands.add(newH);
		}
		return possibleHands;
	}
	
	private static Hand createBestHand(Hand h){
		int numJokers = numberOfJokers(h);
		ArrayList<Hand> allHands = new ArrayList<Hand>();
		if(numJokers == 1){
			allHands = createPossibleHands(h);
		}
		else if(numJokers == 2){
			ArrayList<Hand> tempHands = createPossibleHands(h);
			for(Hand temph : tempHands){
				ArrayList<Hand> tempHands2 = createPossibleHands(temph);
				allHands.addAll(tempHands2);
			}
		}
		else if(numJokers == 3){
			ArrayList<Hand> tempHands = createPossibleHands(h);
			for(Hand temph : tempHands){
				ArrayList<Hand> tempHands2 = createPossibleHands(temph);
				for(Hand temph2 : tempHands2){
					ArrayList<Hand> tempHands3 = createPossibleHands(temph2);
					allHands.addAll(tempHands3);
				}
			}
		}
		else if(numJokers == 4){
			Card lastCard = h.CardsInHand.get(4);
			ArrayList<Card> newHCards = new ArrayList<Card>();
			Hand newH = new Hand();
			newHCards.add(lastCard);
			newHCards.add(lastCard);
			newHCards.add(lastCard);
			newHCards.add(lastCard);
			newHCards.add(lastCard);
			newH.setCardsInHand(newHCards);
			return newH;
		}
		else if(numJokers == 5){
			Card jokerAce = new Card(eSuit.SPADES, eRank.ACE, 53);
			ArrayList<Card> newHCards = new ArrayList<Card>();
			Hand newH = new Hand();
			newHCards.add(jokerAce);
			newHCards.add(jokerAce);
			newHCards.add(jokerAce);
			newHCards.add(jokerAce);
			newHCards.add(jokerAce);
			newH.setCardsInHand(newHCards);
			return newH;
		}
		return PickHand(allHands);
	}

	public ArrayList<Card> getCardsInHand() {
		return CardsInHand;
	}

	private void setCardsInHand(ArrayList<Card> cardsInHand) {
		CardsInHand = cardsInHand;
	}

	private ArrayList<Card> getBestCardsInHand() {
		return BestCardsInHand;
	}

	private void setBestCardsInHand(ArrayList<Card> bestCardsInHand) {
		BestCardsInHand = bestCardsInHand;
	}

	public HandScore getHandScore() {
		return HandScore;
	}

	private void setHandScore(HandScore handScore) {
		HandScore = handScore;
	}

	public boolean isbScored() {
		return bScored;
	}

	private void setbScored(boolean bScored) {
		this.bScored = bScored;
	}

	private Hand AddCardToHand(Card c) {
		CardsInHand.add(c);
		return this;
	}

	public Hand Draw(Deck d) throws DeckException {
		CardsInHand.add(d.Draw());
		return this;
	}

	public void EvalHandScore(Hand h){
		HandScore hs = new HandScore();
		int hStrength = 0;
		if(isHandFiveOfAKind(h, hs)){
			hStrength = eHandStrength.FiveOfAKind.getHandStrength();
		}
		else if(isHandNaturalRoyalFlush(h, hs)){
			hStrength = eHandStrength.NaturalRoyalFlush.getHandStrength();
		}
		else if(isHandRoyalFlush(h,hs)){
			hStrength = eHandStrength.RoyalFlush.getHandStrength();
		}
		else if(isHandStraightFlush(h, hs)){
			hStrength = eHandStrength.StraightFlush.getHandStrength();
		}
		else if(isHandFullHouse(h,hs)){
			hStrength = eHandStrength.FullHouse.getHandStrength();
		}
		else if(isHandFlush(h, hs)){
			hStrength = eHandStrength.Flush.getHandStrength();
		}
		else if(isHandStraight(h, hs)){
			hStrength = eHandStrength.Straight.getHandStrength();
		}
		else if(isHandThreeOfAKind(h, hs)){
			hStrength = eHandStrength.ThreeOfAKind.getHandStrength();
		}
		else if(isHandTwoPair(h, hs)){
			hStrength = eHandStrength.TwoPair.getHandStrength();
		}
		else if(isHandPair(h, hs)){
			hStrength = eHandStrength.Pair.getHandStrength();
		}
		else if(isHandHighCard(h, hs)){
			hStrength = eHandStrength.HighCard.getHandStrength();
		}
		HandScore = new HandScore(hStrength, HandScore.getHiHand(), HandScore.getLoHand(), HandScore.getKickers()); 
	}
		
	public static Hand PickHand(ArrayList<Hand> listH){
		Hand h = new Hand();
		ArrayList<Hand> handList = listH; 
		int hs1 = handList.get(0).getHandScore().getHandStrength(); 
		int hs2 = handList.get(1).getHandScore().getHandStrength();
		while(listH.size() > 1){
			if(hs2 > hs1){
				handList.remove(0);
			}
			else{
				handList.remove(1);
			}
		}
		return handList.get(0);
	}
	
	/**
	 * EvaluateHand is a static method that will score a given Hand of cards
	 * 
	 * @param h
	 * @return
	 * @throws HandException
	 */
	public static Hand EvaluateHand(Hand h) throws HandException {

		Collections.sort(h.getCardsInHand());

		// Collections.sort(h.getCardsInHand(), Card.CardRank);

		if (h.getCardsInHand().size() != 5) {
			throw new HandException(h);
		}

		HandScore hs = new HandScore();
		try {
			Class<?> c = Class.forName("pokerBase.Hand");

			for (eHandStrength hstr : eHandStrength.values()) {
				Class[] cArg = new Class[2];
				cArg[0] = pokerBase.Hand.class;
				cArg[1] = pokerBase.HandScore.class;

				Method meth = c.getMethod(hstr.getEvalMethod(), cArg);
				Object o = meth.invoke(null, new Object[] { h, hs });

				// If o = true, that means the hand evaluated- skip the rest of
				// the evaluations
				if ((Boolean) o) {
					break;
				}
			}

			h.bScored = true;
			h.HandScore = hs;

		} catch (ClassNotFoundException x) {
			x.printStackTrace();
		} catch (IllegalAccessException x) {
			x.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return h;
	}

	private static boolean isHandFlush(ArrayList<Card> cards) {
		int cnt = 0;
		boolean bIsFlush = false;
		for (eSuit Suit : eSuit.values()) {
			cnt = 0;
			for (Card c : cards) {
				if (c.geteSuit() == Suit) {
					cnt++;
				}
			}
			if (cnt == 5)
				bIsFlush = true;

		}
		return bIsFlush;
	}

	private static boolean isStraight(ArrayList<Card> cards, Card highCard) {
		boolean bIsStraight = false;
		boolean bAce = false;

		int iStartCard = 0;
		highCard.seteRank(cards.get(eCardNo.FirstCard.getCardNo()).geteRank());
		highCard.seteSuit(cards.get(eCardNo.FirstCard.getCardNo()).geteSuit());

		if (cards.get(eCardNo.FirstCard.getCardNo()).geteRank() == eRank.ACE) {
			// First card is an 'ace', handle aces
			bAce = true;
			iStartCard++;
		}

		for (int a = iStartCard; a < cards.size() - 1; a++) {
			if ((cards.get(a).geteRank().getiRankNbr() - cards.get(a + 1).geteRank().getiRankNbr()) == 1) {
				bIsStraight = true;
			} else {
				bIsStraight = false;
				break;
			}
		}

		if ((bAce) && (bIsStraight)) {
			if (cards.get(eCardNo.SecondCard.getCardNo()).geteRank() == eRank.KING) {
				highCard.seteRank(cards.get(eCardNo.FirstCard.getCardNo()).geteRank());
				highCard.seteSuit(cards.get(eCardNo.FirstCard.getCardNo()).geteSuit());
			} else if (cards.get(eCardNo.SecondCard.getCardNo()).geteRank() == eRank.FIVE) {
				highCard.seteRank(cards.get(eCardNo.SecondCard.getCardNo()).geteRank());
				highCard.seteSuit(cards.get(eCardNo.SecondCard.getCardNo()).geteSuit());
			} else {
				bIsStraight = false;
			}
		}
		return bIsStraight;
	}

	public static boolean isHandFiveOfAKind(Hand h, HandScore hs) {

		int iCnt = 0;
		boolean isFive = false;

		for (eRank Rank : eRank.values()) {
			iCnt = 0;
			for (Card c : h.getCardsInHand()) {
				if (c.geteRank() == Rank) {
					iCnt++;
				}
			}
			if (iCnt == 5) {
				isFive = true;
				break;
			}
		}

		if (isFive) {
			hs.setHandStrength(eHandStrength.FiveOfAKind.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
		}
		return isFive;
	}

	public static boolean isHandNaturalRoyalFlush(Hand h, HandScore hs) {
		Card c = new Card();
		boolean isRoyalFlush = false;
		if(numberOfJokers(h) == 0){
			if ((isHandFlush(h.getCardsInHand())) && (isStraight(h.getCardsInHand(), c))) {
				if (c.geteRank() == eRank.ACE) {
					isRoyalFlush = true;
					hs.setHandStrength(eHandStrength.RoyalFlush.getHandStrength());
					hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
					hs.setLoHand(0);
				} 
			}
		}
		return isRoyalFlush;
	}

	public static boolean isHandRoyalFlush(Hand h, HandScore hs) {

		Card c = new Card();
		boolean isRoyalFlush = false;
		if ((isHandFlush(h.getCardsInHand())) && (isStraight(h.getCardsInHand(), c))) {
			if (c.geteRank() == eRank.ACE) {
				isRoyalFlush = true;
				hs.setHandStrength(eHandStrength.RoyalFlush.getHandStrength());
				hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
				hs.setLoHand(0);
			}

		}

		return isRoyalFlush;
	}

	public static boolean isHandStraightFlush(Hand h, HandScore hs) {
		Card c = new Card();
		boolean isRoyalFlush = false;
		if ((isHandFlush(h.getCardsInHand())) && (isStraight(h.getCardsInHand(), c))) {
			isRoyalFlush = true;
			hs.setHandStrength(eHandStrength.StraightFlush.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
		}

		return isRoyalFlush;
	}

	public static boolean isHandFourOfAKind(Hand h, HandScore hs) {

		boolean bHandCheck = false;

		if (h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.FourthCard.getCardNo()).geteRank()) {
			bHandCheck = true;
			hs.setHandStrength(eHandStrength.FourOfAKind.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			ArrayList<Card> kickers = new ArrayList<Card>();
			kickers.add(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));
			hs.setKickers(kickers);

		} else if (h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.FifthCard.getCardNo()).geteRank()) {
			bHandCheck = true;
			hs.setHandStrength(eHandStrength.FourOfAKind.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			ArrayList<Card> kickers = new ArrayList<Card>();
			kickers.add(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()));
			hs.setKickers(kickers);
		}

		return bHandCheck;
	}

	public static boolean isHandFullHouse(Hand h, HandScore hs) {

		boolean isFullHouse = false;
		ArrayList<Card> kickers = new ArrayList<Card>();
		if ((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.ThirdCard.getCardNo()).geteRank())
				&& (h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FifthCard.getCardNo()).geteRank())) {
			isFullHouse = true;
			hs.setHandStrength(eHandStrength.FullHouse.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank().getiRankNbr());
		} else if ((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.SecondCard.getCardNo()).geteRank())
				&& (h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FifthCard.getCardNo()).geteRank())) {
			isFullHouse = true;
			hs.setHandStrength(eHandStrength.FullHouse.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
		}

		return isFullHouse;

	}

	public static boolean isHandFlush(Hand h, HandScore hs) {

		boolean bIsFlush = false;
		if (isHandFlush(h.getCardsInHand())) {
			hs.setHandStrength(eHandStrength.Flush.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			ArrayList<Card> kickers = new ArrayList<Card>();
			kickers.add(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()));
			kickers.add(h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()));
			kickers.add(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()));
			kickers.add(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));
			hs.setKickers(kickers);
			bIsFlush = true;
		}

		return bIsFlush;
	}

	public static boolean isHandStraight(Hand h, HandScore hs) {

		boolean bIsStraight = false;
		Card highCard = new Card();
		if (isStraight(h.getCardsInHand(), highCard)) {
			hs.setHandStrength(eHandStrength.Straight.getHandStrength());
			hs.setHiHand(highCard.geteRank().getiRankNbr());
			hs.setLoHand(0);
			bIsStraight = true;
		}
		return bIsStraight;
	}

	public static boolean isHandThreeOfAKind(Hand h, HandScore hs) {

		boolean isThreeOfAKind = false;
		ArrayList<Card> kickers = new ArrayList<Card>();
		if (h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.ThirdCard.getCardNo()).geteRank()) {
			isThreeOfAKind = true;
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			kickers.add(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()));
			kickers.add(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));
		} else if (h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.FourthCard.getCardNo()).geteRank()) {
			isThreeOfAKind = true;
			hs.setHiHand(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr());
			kickers.add(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()));
			kickers.add(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));

		} else if (h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.FifthCard.getCardNo()).geteRank()) {
			isThreeOfAKind = true;
			hs.setHiHand(h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank().getiRankNbr());
			kickers.add(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()));
			kickers.add(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()));

		}

		if (isThreeOfAKind) {
			hs.setHandStrength(eHandStrength.ThreeOfAKind.getHandStrength());
			hs.setLoHand(0);
			hs.setKickers(kickers);
		}

		return isThreeOfAKind;
	}

	public static boolean isHandTwoPair(Hand h, HandScore hs) {

		boolean isTwoPair = false;
		ArrayList<Card> kickers = new ArrayList<Card>();
		if ((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.SecondCard.getCardNo()).geteRank())
				&& (h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FourthCard.getCardNo()).geteRank())) {
			isTwoPair = true;
			hs.setHandStrength(eHandStrength.TwoPair.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank().getiRankNbr());
			kickers.add(h.getCardsInHand().get((eCardNo.FifthCard.getCardNo())));
			hs.setKickers(kickers);
		} else if ((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.SecondCard.getCardNo()).geteRank())
				&& (h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FifthCard.getCardNo()).geteRank())) {
			isTwoPair = true;
			hs.setHandStrength(eHandStrength.TwoPair.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank().getiRankNbr());
			kickers.add(h.getCardsInHand().get((eCardNo.ThirdCard.getCardNo())));
			hs.setKickers(kickers);
		} else if ((h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.ThirdCard.getCardNo()).geteRank())
				&& (h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FifthCard.getCardNo()).geteRank())) {
			isTwoPair = true;
			hs.setHandStrength(eHandStrength.TwoPair.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank().getiRankNbr());
			kickers.add(h.getCardsInHand().get((eCardNo.FirstCard.getCardNo())));
			hs.setKickers(kickers);
		}
		return isTwoPair;
	}

	public static boolean isHandPair(Hand h, HandScore hs) {
		boolean isPair = false;
		ArrayList<Card> kickers = new ArrayList<Card>();
		if (h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.SecondCard.getCardNo()).geteRank()) {
			isPair = true;
			hs.setHandStrength(eHandStrength.Pair.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			kickers.add(h.getCardsInHand().get((eCardNo.ThirdCard.getCardNo())));
			kickers.add(h.getCardsInHand().get((eCardNo.FourthCard.getCardNo())));
			kickers.add(h.getCardsInHand().get((eCardNo.FifthCard.getCardNo())));
			hs.setKickers(kickers);
		} else if (h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.ThirdCard.getCardNo()).geteRank()) {
			isPair = true;
			hs.setHandStrength(eHandStrength.Pair.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			kickers.add(h.getCardsInHand().get((eCardNo.FirstCard.getCardNo())));
			kickers.add(h.getCardsInHand().get((eCardNo.FourthCard.getCardNo())));
			kickers.add(h.getCardsInHand().get((eCardNo.FifthCard.getCardNo())));
			hs.setKickers(kickers);
		} else if (h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.FourthCard.getCardNo()).geteRank()) {
			isPair = true;
			hs.setHandStrength(eHandStrength.Pair.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			kickers.add(h.getCardsInHand().get((eCardNo.FirstCard.getCardNo())));
			kickers.add(h.getCardsInHand().get((eCardNo.SecondCard.getCardNo())));
			kickers.add(h.getCardsInHand().get((eCardNo.FifthCard.getCardNo())));
			hs.setKickers(kickers);
		} else if (h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.FifthCard.getCardNo()).geteRank()) {
			isPair = true;
			hs.setHandStrength(eHandStrength.Pair.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			kickers.add(h.getCardsInHand().get((eCardNo.FirstCard.getCardNo())));
			kickers.add(h.getCardsInHand().get((eCardNo.SecondCard.getCardNo())));
			kickers.add(h.getCardsInHand().get((eCardNo.ThirdCard.getCardNo())));
			hs.setKickers(kickers);
		}
		return isPair;
	}

	public static boolean isHandHighCard(Hand h, HandScore hs) {
		hs.setHandStrength(eHandStrength.HighCard.getHandStrength());
		hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
		hs.setLoHand(0);
		ArrayList<Card> kickers = new ArrayList<Card>();
		kickers.add(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()));
		kickers.add(h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()));
		kickers.add(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()));
		kickers.add(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));
		hs.setKickers(kickers);
		return true;
	}

	public static Comparator<Hand> HandRank = new Comparator<Hand>() {

		public int compare(Hand h1, Hand h2) {

			int result = 0;

			result = h2.getHandScore().getHandStrength() - h1.getHandScore().getHandStrength();

			if (result != 0) {
				return result;
			}

			result = h2.getHandScore().getHiHand() - h1.getHandScore().getHiHand();
			if (result != 0) {
				return result;
			}

			result = h2.getHandScore().getLoHand() - h1.getHandScore().getLoHand();
			if (result != 0) {
				return result;
			}

			if (h2.getHandScore().getKickers().size() > 0) {
				if (h1.getHandScore().getKickers().size() > 0) {
					result = h2.getHandScore().getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr()
							- h1.getHandScore().getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank()
									.getiRankNbr();
				}
				if (result != 0) {
					return result;
				}
			}

			if (h2.getHandScore().getKickers().size() > 1) {
				if (h1.getHandScore().getKickers().size() > 1) {
					result = h2.getHandScore().getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr()
							- h1.getHandScore().getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank()
									.getiRankNbr();
				}
				if (result != 0) {
					return result;
				}
			}

			if (h2.getHandScore().getKickers().size() > 2) {
				if (h1.getHandScore().getKickers().size() > 2) {
					result = h2.getHandScore().getKickers().get(eCardNo.ThirdCard.getCardNo()).geteRank().getiRankNbr()
							- h1.getHandScore().getKickers().get(eCardNo.ThirdCard.getCardNo()).geteRank()
									.getiRankNbr();
				}
				if (result != 0) {
					return result;
				}
			}

			if (h2.getHandScore().getKickers().size() > 3) {
				if (h1.getHandScore().getKickers().size() > 3) {
					result = h2.getHandScore().getKickers().get(eCardNo.FourthCard.getCardNo()).geteRank().getiRankNbr()
							- h1.getHandScore().getKickers().get(eCardNo.FourthCard.getCardNo()).geteRank()
									.getiRankNbr();
				}
				if (result != 0) {
					return result;
				}
			}
			return 0;
		}
	};

}
