enum MessageType {
  Text,
  Pic,
  Vido,
  Sign,
  Time,
}

class ChatMessage {
  String text = "";
  String? pic = "";
  String? avatar = "";
  MessageType type = MessageType.Text;
  bool isSender = true;
  DateTime date;

  ChatMessage({
    required this.text,
    required this.type,
    required this.date,
    this.avatar,
    this.isSender = true,
    this.pic,
  });
}
